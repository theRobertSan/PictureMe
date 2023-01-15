package com.example.pictureme.data.interfaces

import android.net.Uri
import com.example.pictureme.data.Response
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.PreviewPicme
import com.google.firebase.firestore.DocumentReference
import java.io.File

interface PicmeRepository {
    suspend fun storePicmeImage(userId: String, imageUri: Uri): String
    suspend fun addPicme(previewPicme: PreviewPicme): Picme
    suspend fun loadPicmes(userId: String): List<Picme>
    suspend fun loadPicmeImage(userId: String, imageUrl: String): File
    suspend fun loadFeelings(): List<Feeling>
    suspend fun loadPicme(picmeReference: DocumentReference): Picme
}