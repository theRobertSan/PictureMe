package com.example.pictureme.data.interfaces

import android.net.Uri
import com.example.pictureme.data.Resource
import com.example.pictureme.data.models.Picme

interface PicmeRepository {
    suspend fun storePicmeImage(userId: String, imageUri: Uri): Resource<Uri>
    suspend fun addPicme(userId: String, downloadUri: Uri): Resource<Boolean>
    suspend fun loadPicmes(userId: String): Resource<ArrayList<Picme>>

}