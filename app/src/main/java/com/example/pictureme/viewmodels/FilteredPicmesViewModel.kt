package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.models.Picme
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilteredPicmesViewModel @Inject constructor() : ViewModel() {
    // Filtered Picme's
    private val _filteredPicmesLiveData = MutableLiveData<List<Picme>>()
    val filteredPicmesLiveData: LiveData<List<Picme>> = _filteredPicmesLiveData

    fun addPicme(picme: Picme) = viewModelScope.launch {
        // Add created picme to current picme list
        _filteredPicmesLiveData.postValue(filteredPicmesLiveData.value!! + picme)
    }

    fun getPicme(picmeId: String): Picme {
        return _filteredPicmesLiveData.value!!.find { it.id == picmeId }!!
    }

    fun addPicmeList(picmes: List<Picme>)= viewModelScope.launch {
        _filteredPicmesLiveData.postValue(picmes)

        /*if(_filteredPicmesLiveData.value == null) {
            val res = _filteredPicmesLiveData.postValue(picmes)
        }else{
            for(picme in picmes){
                addPicme(picme)
            }
        }*/
    }
}

