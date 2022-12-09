package com.example.pictureme.data.repository

import com.example.pictureme.data.Resource
import com.example.pictureme.data.interfaces.UserRepository
import com.example.pictureme.data.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    firestore: FirebaseFirestore
) : UserRepository {

    private val userCollection = firestore.collection("users")

    override suspend fun addUser(id: String, username: String): Resource<User> {
        val user = hashMapOf(
            "username" to username
        )

        return try {
            userCollection.document(id).set(user).await()
            Resource.Success(User(id, username))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }

    override suspend fun loadUser(id: String): Resource<User> {
        return try {
            lateinit var result : Resource.Success<User>
            userCollection.document(id).get().addOnSuccessListener { documentSnapshot ->
                    result = Resource.Success(documentSnapshot.toObject<User>()!!)
            }.await()

//            val result = userCollection.document(id).get().await()
            println(result)
            println(result.result)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }

}