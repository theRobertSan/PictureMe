package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.models.Filter
import com.example.pictureme.data.models.Picme
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilteredPicmesViewModel @Inject constructor() : ViewModel() {
    // Filtered Picme's
    private val _filteredPicmesLiveData = MutableLiveData<List<Picme>?>()
    val filteredPicmesLiveData: LiveData<List<Picme>?> = _filteredPicmesLiveData

    private val _filterLiveData = MutableLiveData<Filter>(Filter())
    val filterLiveData: LiveData<Filter> = _filterLiveData

    fun addPicmeList(picmes: List<Picme>) = viewModelScope.launch {
        _filteredPicmesLiveData.postValue(picmes)
        _filteredPicmesLiveData.value = null
    }

    fun updateFilter(filter: Filter) = viewModelScope.launch {
        _filterLiveData.postValue(filter)
    }

}

