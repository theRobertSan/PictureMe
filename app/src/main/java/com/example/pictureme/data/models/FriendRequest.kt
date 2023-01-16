package com.example.pictureme.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class FriendRequest (
    @DocumentId
    var id: String?,
    @Exclude
    var sendingUser: User?,
    var sentAt: Timestamp?
    ): Serializable {
    constructor() : this(null, null, null)
}