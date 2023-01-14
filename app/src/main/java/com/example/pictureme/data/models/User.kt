package com.example.pictureme.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class User(
    @DocumentId
    var id: String?,
    var username: String?,
    @Exclude
    var friendships: List<Friendship>?
) : Serializable {
    constructor() : this(null, null, null)
}