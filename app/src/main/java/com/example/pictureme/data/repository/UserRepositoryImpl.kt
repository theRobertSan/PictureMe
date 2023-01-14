package com.example.pictureme.data.repository

import com.example.pictureme.data.Response
import com.example.pictureme.data.interfaces.UserRepository
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : UserRepository {

    override var currentUser: User? = null
    override var friendships: List<Friendship>? = null

    private val userCollection = firestore.collection("users")
    private val friendshipCollection = firestore.collection("friendships")

    override suspend fun addUser(id: String, username: String): User {
        val user = hashMapOf(
            "username" to username
        )

        userCollection.document(id).set(user).await()
        currentUser = User(id, username, listOf())
        return currentUser!!
    }

    override suspend fun loadUser(id: String): User {
        // Get user document
        val userReference = userCollection.document(id)

        // Load user
        currentUser = userReference.get().await().toObject<User>()

        // Load its friendships
        val friendships = ArrayList<Friendship>()

        // User is on the left
        val userFriendships1 = friendshipCollection
            .whereEqualTo("user1Ref", userReference)
            .get()
            .await()
        println(userReference)
        println("---------------------------")
        println(userFriendships1)
        // User is on the right
        val userFriendships2 = friendshipCollection
            .whereEqualTo("user2Ref", userReference)
            .get()
            .await()

        // Iterate over userFriendships
        for (userFriendship in (userFriendships1 + userFriendships2)) {
            val friendship = userFriendship.toObject<Friendship>()
            // Select friend from user1 and user2 and get him (as User)
            val friend =
                if (userFriendship.data["user1Ref"]!! == userReference) userFriendship.data["user2Ref"]!! else userFriendship.data["user1Ref"]!!
            friendship.friend = (friend as DocumentReference).get().await().toObject<User>()

            // Add friendship to current users list
            friendships.add(friendship)
        }

        currentUser!!.friendships = friendships

        println(currentUser)

        return currentUser!!
    }

}