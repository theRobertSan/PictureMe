package com.example.pictureme.data.models

import android.net.Uri
import com.google.firebase.firestore.GeoPoint

data class PreviewPicme(
    var creatorId: String,
    var friendIds: MutableList<String> = mutableListOf(),
    // Default feeling is happy
    var feeling: String = "4oZe0eAp3spsDfsySAIt",
    var imageUri: Uri? = null,
    var imagePath: String? = null,
    var location: GeoPoint? = null
)