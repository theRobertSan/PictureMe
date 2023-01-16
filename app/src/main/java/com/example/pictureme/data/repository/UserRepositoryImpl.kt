package com.example.pictureme.data.repository

import com.example.pictureme.data.interfaces.UserRepository
import com.example.pictureme.data.models.FriendRequest
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class UserRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    val firestorage: FirebaseStorage
) : UserRepository {

    override var currentUser: User? = null
    override var friendships: List<Friendship>? = null
    override var friendRequests: List<FriendRequest>? = null

    private val userCollection = firestore.collection("users")
    private val friendshipCollection = firestore.collection("friendships")
    private val friendRequestCollection = firestore.collection("friendRequests")

    override suspend fun addUser(id: String, username: String, fullName: String): User {
        val user = hashMapOf(
            "username" to username,
            "fullName" to fullName
        )

        userCollection.document(id).set(user).await()
        currentUser = User(id, username, fullName, null, listOf(), listOf())
        return currentUser!!
    }

    override suspend fun loadUser(id: String): User {
        // Get user document
        val userReference = userCollection.document(id)
        // Load user
        currentUser = userReference.get().await().toObject<User>()

        loadUserFriendships(userReference, currentUser)
        loadUserFriendRequests(userReference, currentUser)

        return currentUser!!
    }

    private suspend fun loadUserFriendships(
        userReference: DocumentReference,
        currentUser: User?
    ): User {
        // Load its friendships
        val friendships = ArrayList<Friendship>()

        // User is on the left
        val userFriendships1 = friendshipCollection
            .whereEqualTo("user1Ref", userReference)
            .get()
            .await()

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

        this.currentUser!!.friendships = friendships

        return this.currentUser!!
    }

    private suspend fun loadUserFriendRequests(
        userReference: DocumentReference,
        currentUser: User?
    ): User {
        // Load the friend requests
        val loadedFriendRequests = ArrayList<FriendRequest>()

        val requestsReceived = friendRequestCollection
            .whereEqualTo("receiverRef", userReference)
            .get()
            .await()

        // Iterate over requestsReceived
        for (request in requestsReceived) {
            val friendRequest = request.toObject<FriendRequest>()
            // Set the sending User which sent the request
            val sendingUser = request.data["creatorRef"]!!
            friendRequest.sendingUser =
                (sendingUser as DocumentReference).get().await().toObject<User>()
            // Add friend request to current users list
            loadedFriendRequests.add(friendRequest)
        }

        this.currentUser!!.friendRequests = loadedFriendRequests

        return this.currentUser!!
    }

    override suspend fun createFriendRequest(username: String, currentUserId: String) {
        val currentUserRef = userCollection.document(currentUserId)
        val otherUserRef = userCollection
            .whereEqualTo("username", username).get().await().documents[0].reference

        val friendRequest = hashMapOf(
            "creatorRef" to currentUserRef,
            "receiverRef" to otherUserRef,
            "sentAt" to Timestamp(Date())
        )

        friendRequestCollection.add(friendRequest).await()
    }

    override suspend fun handleFriendRequestAnswer(requestId: String, accepted: Boolean) : Friendship? {
        val currentRequest = friendRequestCollection.document(requestId).get().await()
        var friendshipObj: Friendship? = null

        // If the friend request has been accepted
        if(accepted) {
            val friendship = hashMapOf(
                "user1Ref" to currentRequest.data!!["creatorRef"] as DocumentReference,
                "user2Ref" to currentRequest.data!!["receiverRef"] as DocumentReference,
                "beganAt" to Timestamp(Date())
            )
            val friendshipSnapshot = friendshipCollection.add(friendship).await().get().await()
            // Populate friend from friendship
            val friendObj = (friendshipSnapshot.data!!["user1Ref"] as DocumentReference).get().await().toObject<User>()
            friendshipObj = friendshipSnapshot.toObject<Friendship>()
            friendshipObj!!.friend = friendObj
        }

        // Delete friend request from database
        friendRequestCollection.document(requestId).delete()

        return friendshipObj
    }

}