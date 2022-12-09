package com.example.pictureme.data.interfaces

import android.net.Uri
import com.example.pictureme.data.Resource
import com.example.pictureme.data.models.Picme
import java.io.File

interface PicmeRepository {
    suspend fun storePicmeImage(userId: String, imageUri: Uri): Resource<String>
    suspend fun addPicme(userId: String, imagePath: String): Resource<Picme>
    suspend fun loadPicmes(userId: String): Resource<ArrayList<Picme>>
    suspend fun loadPicmeImage(userId: String, imageUrl: String): Resource<File>

}