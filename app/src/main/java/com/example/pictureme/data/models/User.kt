package com.example.pictureme.data.models

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String,
    val username: String
) {
    constructor() : this("", "")
}