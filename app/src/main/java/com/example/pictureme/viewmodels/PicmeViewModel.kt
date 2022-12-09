package com.example.pictureme.viewmodels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.Resource
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.interfaces.PicmeRepository
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicmeViewModel @Inject constructor(
  private val picmeRepository: PicmeRepository,
  private val authRepository: AuthRepository
): ViewModel() {

    private val _picmesLiveData = MutableLiveData<Resource<ArrayList<Picme>>?>()
    val picmesLiveData: LiveData<Resource<ArrayList<Picme>>?> = _picmesLiveData

    private val currentUserId = authRepository.currentUser!!.uid

    fun addPicme(imageUri: Uri) = viewModelScope.launch {
        // Save to storage
        when(val resultStorage = picmeRepository.storePicmeImage(currentUserId, imageUri)) {
            // Save to firebase
            is Resource.Success -> picmeRepository.addPicme(currentUserId, resultStorage.result)
            else -> Log.e(TAG, "Error saving image to storage")
        }
    }

    fun loadPicmes() = viewModelScope.launch {
        val result = picmeRepository.loadPicmes(currentUserId)
        _picmesLiveData.value = result
    }

}