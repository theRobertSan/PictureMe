package com.example.pictureme.data.interfaces

import android.net.Uri
import com.example.pictureme.data.models.FriendRequest
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.User

interface UserRepository {
    var currentUser: User?
    var friendships: List<Friendship>?
    var friendRequests: List<FriendRequest>?
    suspend fun addUser(id: String, username: String, fullName: String): User
    suspend fun loadUser(id: String): User
    suspend fun createFriendRequest(username: String, currentUserId: String)
    suspend fun handleFriendRequestAnswer(requestId: String, accepted: Boolean): Friendship?
    suspend fun storeProfileImage(currentUserId: String, imageUri: Uri): String
    suspend fun updateUserProfilePicture(currentUserId: String, profilePicturePath: String)
}