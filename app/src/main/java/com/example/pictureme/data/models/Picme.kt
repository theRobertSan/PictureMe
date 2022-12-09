package com.example.pictureme.data.models

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Picme(
    @DocumentId
    var id: String = "",
    var localUri: String = "",
    var remoteUri: String = "",
    var dateTaken: Date = Date()
)