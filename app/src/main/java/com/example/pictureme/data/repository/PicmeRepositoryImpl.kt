package com.example.pictureme.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.pictureme.data.Resource
import com.example.pictureme.data.interfaces.PicmeRepository
import com.example.pictureme.data.models.Picme
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PicmeRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    firestorage: FirebaseStorage
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
            val userReference = userCollection.document(userId)
            val userPicmeDocs = picmeCollection
                .whereEqualTo("userId", userReference)
                .get()
                .await()

            val picmes = ArrayList<Picme>()
            for (userPicme in userPicmeDocs) {
                Log.i(TAG, "Loading PicMe ${userPicme.data["picmeId"]}")
                val picme = picmeCollection.document(userPicme.data["picmeId"].toString()).get().await()
                picmes.add(picme.toObject<Picme>()!!)
            }

            println(picmes)
            Resource.Success(picmes)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun storePicmeImage(userId: String, imageUri: Uri): Resource<Uri> {
        // Get reference to storage location
        val picmeRef = storageRef.child("${folder}/${userId}/${imageUri.lastPathSegment}")
        return try {
            // Save picture to storage and return download url
            val downloadUrl = picmeRef
                .putFile(imageUri)
                .await()
                .storage.downloadUrl.await()
            Resource.Success(downloadUrl)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun addPicme(userId: String, imageUri: Uri) : Resource<Boolean> {
        val picme = hashMapOf(
            "creator" to userCollection.document(userId),
            "createdAt" to Timestamp(Date()),
            "imageUri" to imageUri
        )

        return try {
            val result = picmeCollection.add(picme).await()
            val userPicme = hashMapOf(
                "userId" to userCollection.document(userId),
                "picmeId" to picmeCollection.document(result.id)
            )
            userPicmeCollection.add(userPicme).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}