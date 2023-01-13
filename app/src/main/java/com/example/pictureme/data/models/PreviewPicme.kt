package com.example.pictureme.data.models

import android.net.Uri

data class PreviewPicme(
    var creatorId: String,
    var friendIds: MutableList<String> = mutableListOf(),
    var feeling: String = "Happy",
    var imagePath: Uri? = null
)