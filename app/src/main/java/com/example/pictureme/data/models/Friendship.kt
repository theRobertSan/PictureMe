package com.example.pictureme.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Friendship(
    @DocumentId
    var id: String?,
    @Exclude
    var friend: User?,
    var beganAt: Timestamp?
) : Serializable {
    constructor() : this(null, null, null)
}