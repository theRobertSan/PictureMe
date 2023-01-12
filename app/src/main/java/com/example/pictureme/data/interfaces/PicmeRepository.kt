package com.example.pictureme.data.interfaces

import android.net.Uri
import com.example.pictureme.data.Response
import com.example.pictureme.data.models.Picme
import java.io.File

interface PicmeRepository {
    suspend fun storePicmeImage(userId: String, imageUri: Uri): String
    suspend fun addPicme(userId: String, imagePath: String): Picme
    suspend fun loadPicmes(userId: String): List<Picme>
    suspend fun loadPicmeImage(userId: String, imageUrl: String): File

}