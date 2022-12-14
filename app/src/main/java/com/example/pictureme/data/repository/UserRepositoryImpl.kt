package com.example.pictureme.data.repository

import com.example.pictureme.data.Resource
import com.example.pictureme.data.interfaces.UserRepository
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    firestore: FirebaseFirestore
) : UserRepository {

    override var currentUser: User? = null
    override var friendships: List<Friendship>? = null

    private val userCollection = firestore.collection("users")

    override suspend fun addUser(id: String, username: String): Resource<User> {
        val user = hashMapOf(
            "username" to username
        )

        return try {
            userCollection.document(id).set(user).await()
            currentUser = User(id, username, listOf())
            Resource.Success(currentUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }

    override suspend fun loadUser(id: String): Resource<User> {
        return try {
            // Load user
            currentUser = userCollection.document(id).get().await().toObject<User>()

            // Load its friends
            loadFriends()

            Resource.Success(currentUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }

    override suspend fun loadFriends(): Resource<List<Friendship>> {
        return try {
            for (friendship in currentUser!!.friendships!!) {
                friendship.friend = friendship.friendId!!.get().await().toObject<User>()
            }
            Resource.Success(currentUser!!.friendships!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}