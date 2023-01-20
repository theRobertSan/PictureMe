package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.models.Picme
import kotlinx.coroutines.launch

class PicmeDetailsViewModel(
) : ViewModel() {
    // Preview Picme
    private val _picmeLiveData = MutableLiveData<Picme>()
    val picmeLiveData: LiveData<Picme> = _picmeLiveData

    fun selectPicme(picme: Picme) = viewModelScope.launch {
        _picmeLiveData.postValue(picme)
    }

}