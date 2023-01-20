package com.example.pictureme.data.repository

import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    private val usernameCollection = firestore.collection("usernames")

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Response<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun signup(
        username: String,
        email: String,
        password: String
    ): Response<FirebaseUser> {

        // Check if user with username already exists
        if (usernameCollection.document(username).get().await().exists()) {
            return Response.Failure(java.lang.Exception())
        }

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//            result?.user?.updateProfile(
//                UserProfileChangeRequest.Builder().setDisplayName(name).build()
//            )?.await()

            // Add username to firestore
            usernameCollection.document(username).set({}).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}