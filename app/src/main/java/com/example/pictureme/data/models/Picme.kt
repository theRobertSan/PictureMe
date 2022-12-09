package com.example.pictureme.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.File
import java.util.*

data class Picme(
    @DocumentId
    var id: String?,
    var imagePath: String?,
    @Exclude
    var imageFile: File?,
    var creator: DocumentReference?,
    var createdAt: Timestamp?
) {
    constructor() : this(null, null, null, null, null)
}