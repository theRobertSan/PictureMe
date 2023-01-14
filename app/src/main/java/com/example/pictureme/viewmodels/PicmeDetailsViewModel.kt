package com.example.pictureme.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.PreviewPicme
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PicmeDetailsViewModel(
) : ViewModel() {
    // Preview Picme
    private val _picmeLiveData = MutableLiveData<Picme>()
    val picmeLiveData: LiveData<Picme> = _picmeLiveData

    fun selectPicme(picme: Picme) = viewModelScope.launch {
        _picmeLiveData.postValue(picme)
    }

}