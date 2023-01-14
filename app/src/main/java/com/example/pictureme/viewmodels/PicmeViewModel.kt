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
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.PreviewPicme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicmeViewModel @Inject constructor(
    private val picmeRepository: PicmeRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    // User Picme's
    private val _picmesLiveData = MutableLiveData<List<Picme>>()
    val picmesLiveData: LiveData<List<Picme>> = _picmesLiveData

    // Feelings
    private val _foodFeelingsLiveData = MutableLiveData<List<Feeling>>()
    val foodFeelingsLiveData: LiveData<List<Feeling>> = _foodFeelingsLiveData

    private val _nonFoodFeelingsLiveData = MutableLiveData<List<Feeling>>()
    val nonFoodFeelingsLiveData: LiveData<List<Feeling>> = _nonFoodFeelingsLiveData

    private val currentUserId = authRepository.currentUser!!.uid

    fun addPicme(previewPicme: PreviewPicme) = viewModelScope.launch {
        // Save to Firebase Storage
        previewPicme.imagePath =
            picmeRepository.storePicmeImage(currentUserId, previewPicme.imageUri!!)
        // Save to Firestore
        val createdPicme = picmeRepository.addPicme(previewPicme)
        // Add created picme to current picme list
        _picmesLiveData.postValue(picmesLiveData.value!! + createdPicme)
    }

    fun loadPicmes() = viewModelScope.launch {
        val response = picmeRepository.loadPicmes(currentUserId)
        // Post response from firebase
        println("Loaded Images from Firebase")
        _picmesLiveData.postValue(response)
    }

    fun loadFeelings() = viewModelScope.launch {
        val response = picmeRepository.loadFeelings()

        val nonFoodFeelings = mutableListOf<Feeling>()
        val foodFeelings = mutableListOf<Feeling>()
        for (feeling in response) {
            println(feeling.feeling)
            println(feeling.isFoodPic)
            if (feeling.isFoodPic) foodFeelings.add(feeling) else nonFoodFeelings.add(feeling)
        }
        _foodFeelingsLiveData.postValue(foodFeelings)
        _nonFoodFeelingsLiveData.postValue(nonFoodFeelings)
    }

    fun getPicme(picmeId: String): Picme {
        return _picmesLiveData.value!!.find { it.id == picmeId }!!
    }

}