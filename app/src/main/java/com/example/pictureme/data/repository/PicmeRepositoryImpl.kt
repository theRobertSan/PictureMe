package com.example.pictureme.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.pictureme.data.Response
import com.example.pictureme.data.interfaces.PicmeRepository
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
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
    private val feelingCollection = firestore.collection("feelings")

    override suspend fun loadPicmes(userId: String): List<Picme> {
        // Get user document
        val userReference = userCollection.document(userId)

        // Get user-picmes documents for that user
        val userPicmeDocs = userPicmeCollection
            .whereEqualTo("userId", userReference)
            .get()
            .await()

        // Store all picme objects for that user
        val picmes = ArrayList<Picme>()

        // Iterate over user-picmes
        for (userPicme in userPicmeDocs) {
            val picme = (userPicme.data["picmeId"] as DocumentReference).get().await()
            // Convert to Picme object and add to list
            picmes.add(picme.toObject<Picme>()!!)
        }

        // Get images url (for Coil)
        for (picme in picmes) {
            val landRef = storageRef.child(picme.imagePath!!).downloadUrl.await()
            picme.imagePath = landRef.toString()
        }

        // Return picmes
        return picmes
    }

    override suspend fun storePicmeImage(userId: String, imageUri: Uri): String {
        // Get reference to storage location
        val picmeRef = storageRef.child("${folder}/${userId}/${imageUri.lastPathSegment}")
        // Save picture to storage and return download url
        val picturePath = picmeRef
            .putFile(imageUri)
            .await()
            .storage.path

        return picturePath
    }

    override suspend fun loadPicmeImage(userId: String, imagePath: String): File {
        // Get reference to storage location
        val landRef = storageRef.child(imagePath)
        // Save picture to storage and return download url
        val localFile = File.createTempFile("picme", "jpg")
        landRef.getFile(localFile).await()
        return localFile
    }

    override suspend fun addPicme(userId: String, imagePath: String): Picme {
        val picme = hashMapOf(
            "creator" to userCollection.document(userId),
            "createdAt" to Timestamp(Date()),
            "imagePath" to imagePath
        )

        val result = picmeCollection.add(picme).await()
        val userPicme = hashMapOf(
            "userId" to userCollection.document(userId),
            "picmeId" to picmeCollection.document(result.id)
        )
        userPicmeCollection.add(userPicme).await()

        // Get created picme
        val createdPicme = picmeCollection.document(result.id).get().await().toObject<Picme>()!!
        Log.i(TAG, "Created PicMe with id ${createdPicme.id}")

        // Get images url (for Coil)
        createdPicme.imagePath =
            storageRef.child(createdPicme.imagePath!!).downloadUrl.await().toString()

        return createdPicme
    }

    override suspend fun loadFeelings(): List<Feeling> {
        val feelings = feelingCollection
            .get()
            .await()

        return feelings.toObjects()
    }
}