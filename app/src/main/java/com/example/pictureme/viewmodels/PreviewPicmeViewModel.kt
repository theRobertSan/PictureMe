package com.example.pictureme.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.models.PreviewPicme
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewPicmeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    // Preview Picme
    private val _previewLiveData = MutableLiveData(PreviewPicme(authRepository.currentUser!!.uid))
    val previewLiveData: LiveData<PreviewPicme> = _previewLiveData

    fun selectFriend(friendId: String) {
        _previewLiveData.value!!.friendIds.add(friendId)
    }

    fun unselectFriend(friendId: String) {
        _previewLiveData.value!!.friendIds.remove(friendId)
    }

    fun containsFriend(friendId: String): Boolean {
        return _previewLiveData.value!!.friendIds.contains(friendId)
    }

    fun updateImageUri(imageUri: Uri) {
        _previewLiveData.value!!.imageUri = imageUri
    }

    fun updateFeeling(feelingId: String) {
        _previewLiveData.value!!.feeling = feelingId
    }

    fun removeFeeling() {
        _previewLiveData.value!!.feeling = ""
    }

    fun hasFeeling(feelingId: String): Boolean {
        return _previewLiveData.value!!.feeling == feelingId
    }

    fun setLocation(geoPoint: GeoPoint) {
        _previewLiveData.value!!.location = geoPoint
    }

    fun clear() {
        _previewLiveData.postValue(PreviewPicme(authRepository.currentUser!!.uid))
    }

}