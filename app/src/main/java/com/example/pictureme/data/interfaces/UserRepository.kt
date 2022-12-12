package com.example.pictureme.data.interfaces

import com.example.pictureme.data.Resource
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    var currentUser: User?
    var friendships: List<Friendship>?
    suspend fun addUser(id: String, username: String): Resource<User>
    suspend fun loadUser(id: String): Resource<User>
    suspend fun loadFriends(): Resource<List<Friendship>>
}