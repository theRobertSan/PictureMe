package com.example.pictureme.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude

data class Friendship(
    @DocumentId
    var id: String?,
    @Exclude
    var friend: User?,
    var beganAt: Timestamp?
) {
    constructor() : this(null, null, null)
}