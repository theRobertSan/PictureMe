package com.example.pictureme.viewmodels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.Response
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.interfaces.PicmeRepository
import com.example.pictureme.data.models.Picme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicmeViewModel @Inject constructor(
  private val picmeRepository: PicmeRepository,
  private val authRepository: AuthRepository
): ViewModel() {
    // User Picme's
    private val _picmesLiveData = MutableLiveData<List<Picme>>()
    val picmesLiveData: LiveData<List<Picme>> = _picmesLiveData

    private val currentUserId = authRepository.currentUser!!.uid

    fun addPicme(imageUri: Uri) = viewModelScope.launch {
        // Save to Firebase Storage
        val resultStorage = picmeRepository.storePicmeImage(currentUserId, imageUri)
        // Save to Firestore
        val createdPicme = picmeRepository.addPicme(currentUserId, resultStorage)
        // Add created picme to current picme list
        _picmesLiveData.postValue(picmesLiveData.value!! + createdPicme)
    }

    fun loadPicmes() = viewModelScope.launch {
        val response = picmeRepository.loadPicmes(currentUserId)
        // Post response from firebase
        println("Loaded Images from Firebase")
        _picmesLiveData.postValue(response)
    }

}