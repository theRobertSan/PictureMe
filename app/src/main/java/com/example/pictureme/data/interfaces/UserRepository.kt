package com.example.pictureme.data.interfaces

import com.example.pictureme.data.Resource
import com.example.pictureme.data.models.User

interface UserRepository {
    suspend fun addUser(id: String, username: String): Resource<User>
    suspend fun loadUser(id: String): Resource<User>
}