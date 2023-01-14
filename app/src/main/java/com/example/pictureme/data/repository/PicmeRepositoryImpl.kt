package com.example.pictureme.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.pictureme.data.Response
import com.example.pictureme.data.interfaces.PicmeRepository
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.PreviewPicme
import com.example.pictureme.data.models.User
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
            .whereEqualTo("userRef", userReference)
            .get()
            .await()

        // Store all picme objects for that user
        val picmes = ArrayList<Picme>()

        // Iterate over user-picmes
        for (userPicme in userPicmeDocs) {
            val picmeSnapshot = (userPicme.data["picmeRef"] as DocumentReference).get().await()

            val picmeUsers = userPicmeCollection
                .whereEqualTo("picmeRef", userPicme.data["picmeRef"] as DocumentReference)
                .get()
                .await()

            // Convert to Picme object and add to list
            val picmeObj = picmeSnapshot.toObject<Picme>()!!
            val picmeFriends = ArrayList<User>()
            // Convert Friends
            for (picmeUser in picmeUsers) {
                var userReference = (picmeUser.data["userRef"] as DocumentReference)
                var userObj = userReference.get().await().toObject<User>()!!

                // User is creator
                if (userReference == (picmeSnapshot.data!!["creatorRef"] as DocumentReference)) {
                    picmeObj.creator = userObj
                    // User is friend of creator that is in picme
                } else {
                    picmeFriends.add(userObj)
                }

            }
            picmeObj.friends = picmeFriends
            // Convert Feelings
            val picmeFeeling =
                (picmeSnapshot.data?.get("feelingRef") as DocumentReference).get().await()
                    .toObject<Feeling>()
            picmeObj.feeling = picmeFeeling

            picmes.add(picmeObj)
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

    override suspend fun addPicme(previewPicme: PreviewPicme): Picme {
        val picme = hashMapOf(
            "creatorRef" to userCollection.document(previewPicme.creatorId),
            "createdAt" to Timestamp(Date()),
            "imagePath" to previewPicme.imagePath,
            "feelingRef" to feelingCollection.document(previewPicme.feeling),
            "location" to previewPicme.location
        )

        val result = picmeCollection.add(picme).await()
        // Create user-picme documents to represent the connection between the selected friend & the picme
        for (friend in previewPicme.friendIds) {
            val userPicme = hashMapOf(
                "userRef" to userCollection.document(friend),
                "picmeRef" to picmeCollection.document(result.id)
            )
            userPicmeCollection.add(userPicme).await()
        }
        // Also for the creator
        val userPicme = hashMapOf(
            "userRef" to userCollection.document(previewPicme.creatorId),
            "picmeRef" to picmeCollection.document(result.id)
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