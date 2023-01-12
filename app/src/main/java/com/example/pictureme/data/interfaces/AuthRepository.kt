package com.example.pictureme.data.interfaces

import com.example.pictureme.data.Response
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Response<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Response<FirebaseUser>
    fun logout()
}