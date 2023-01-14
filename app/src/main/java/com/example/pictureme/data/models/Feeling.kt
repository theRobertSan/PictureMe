package com.example.pictureme.data.models

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Feeling(
    @DocumentId
    val id: String = "",
    val feeling: String = "",
    @field:JvmField
    val isFoodPic: Boolean = true
) : Serializable
