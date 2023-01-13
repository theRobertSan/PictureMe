package com.example.pictureme.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.models.PreviewPicme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewPicmeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Preview Picme
    private val _previewLiveData = MutableLiveData(PreviewPicme(authRepository.currentUser!!.uid))
    val previewLiveData: LiveData<PreviewPicme> = _previewLiveData

    fun selectFriend(id: String) {
        _previewLiveData.value!!.friendIds.add(id)
        println("ADDED $id")
    }

    fun unselectFriend(id: String) {
        _previewLiveData.value!!.friendIds.remove(id)
        println("REMOVED $id")
    }

    fun containsFriend(id: String): Boolean {
        return _previewLiveData.value!!.friendIds.contains(id)
    }

    fun updateImagePath(imagePath: Uri) {
        _previewLiveData.value!!.imagePath = imagePath
    }

    fun updateFeeling(feeling: String) {
        _previewLiveData.value!!.feeling = feeling
    }

    fun manageFriend(id: String) {

    }


}