package com.example.pictureme.data.models

import android.net.Uri
import com.google.firebase.firestore.GeoPoint

data class Filter(
    var creatorId: String,
    var friendIds: MutableList<String> = mutableListOf()
)