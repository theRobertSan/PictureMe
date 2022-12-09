package com.example.pictureme.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.pictureme.data.Resource
import com.example.pictureme.data.interfaces.PicmeRepository
import com.example.pictureme.data.models.Picme
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PicmeRepositoryImpl @Inject constructor(
    val firestore: FirebaseFirestore,
    val firestorage: FirebaseStorage
) : PicmeRepository {

    // Storage
    private val storageRef = firestorage.reference
    private val folder = "picmes"

    // Firebase
    private val userCollection = firestore.collection("users")
    private val userPicmeCollection = firestore.collection("userPicmes")
    private val picmeCollection = firestore.collection("picmes")

    override suspend fun loadPicmes(userId: String): Resource<ArrayList<Picme>> {
        return try {
            println(userId)
            val userReference = userCollection.document(userId)
            val userPicmeDocs = userPicmeCollection
                .whereEqualTo("userId", userReference)
                .get()
                .await()

            println("-------------")
            println(userPicmeDocs)

            val picmes = ArrayList<Picme>()
            for (userPicme in userPicmeDocs) {
                Log.i(TAG, "Loading PicMe ${userPicme.data["picmeId"]}")
                val picme = (userPicme.data["picmeId"] as DocumentReference).get().await()
                picmes.add(picme.toObject<Picme>()!!)
            }

            // Load images
            for (picme in picmes) {
                val result = loadPicmeImage(userId, picme.imagePath.toString())
                if (result is Resource.Success) {
                    picme.imageFile = result.result
                }

            }

            println("-------------")
            println(picmes)
            Resource.Success(picmes)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun storePicmeImage(userId: String, imageUri: Uri): Resource<String> {
        // Get reference to storage location
        val picmeRef = storageRef.child("${folder}/${userId}/${imageUri.lastPathSegment}")
        return try {
            // Save picture to storage and return download url
            val picturePath = picmeRef
                .putFile(imageUri)
                .await()
                .storage.path

            Resource.Success(picturePath)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun loadPicmeImage(userId: String, imagePath: String): Resource<File> {
        Log.i(TAG, "Loading image from $imagePath.")
        //        return Resource.Success
        // Get reference to storage location
        val landRef = storageRef.child(imagePath)
        return try {
            // Save picture to storage and return download url
            val localFile = File.createTempFile("picme", "jpg")
            landRef.getFile(localFile).await()
            Resource.Success(localFile)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun addPicme(userId: String, imagePath: String) : Resource<Picme> {
        val picme = hashMapOf(
            "creator" to userCollection.document(userId),
            "createdAt" to Timestamp(Date()),
            "imagePath" to imagePath
        )

        return try {
            val result = picmeCollection.add(picme).await()
            val userPicme = hashMapOf(
                "userId" to userCollection.document(userId),
                "picmeId" to picmeCollection.document(result.id)
            )
            userPicmeCollection.add(userPicme).await()

            // Get created picme
            val createdPicme = picmeCollection.document(result.id).get().await()
            Log.i(TAG, "Created PicMe with id ${createdPicme.id}")
            Resource.Success(createdPicme.toObject<Picme>()!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}