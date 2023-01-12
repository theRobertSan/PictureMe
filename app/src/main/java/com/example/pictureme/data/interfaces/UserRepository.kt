package com.example.pictureme.data.interfaces

import com.example.pictureme.data.Response
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.User

interface UserRepository {
    var currentUser: User?
    var friendships: List<Friendship>?
    suspend fun addUser(id: String, username: String): User
    suspend fun loadUser(id: String): User
}