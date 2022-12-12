package com.example.pictureme.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude

data class Friendship(
    var friendId: DocumentReference?,
    @Exclude
    var friend: User?,
    var beganAt: Timestamp?
) {
    constructor() : this(null, null, null)
}