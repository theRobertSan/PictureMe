package com.example.pictureme.data.interfaces

interface PicmeRepository {
    suspend fun loadPicmes()
    suspend fun createPicme()
}