package com.example.pictureme.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

data class User(
    @DocumentId
    var id: String?,
    var username: String?,
    var friendships: List<Friendship>?
) {
    constructor() : this(null, null, null)
}