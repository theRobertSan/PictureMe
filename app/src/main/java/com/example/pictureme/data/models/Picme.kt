package com.example.pictureme.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import java.io.File
import java.io.Serializable
import java.util.*

data class Picme(
    @DocumentId
    var id: String?,
    var imagePath: String?,
    var createdAt: Timestamp?,
    var location: GeoPoint?,
    @Exclude
    var creator: User?,
    @Exclude
    var feeling: Feeling?,
    @Exclude
    var friends: List<User>
) : Serializable {
    constructor() : this(null, null, null, null, null, null, emptyList())
}