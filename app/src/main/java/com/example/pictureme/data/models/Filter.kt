package com.example.pictureme.data.models

import android.net.Uri
import com.google.firebase.firestore.GeoPoint

data class Filter(
    var sortBy: Int? = null,
    var friendIds: ArrayList<String> = ArrayList()
)