package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewPicmeViewModel @Inject constructor(): ViewModel() {

    private val _selectedFriendsLiveData = MutableLiveData<List<DocumentReference>>()
    val selectedFriendsLiveData: LiveData<List<DocumentReference>> = _selectedFriendsLiveData

    private val _selectedFeelingLiveData = MutableLiveData<String>()
    val selectedFeelingLiveData: LiveData<String> = _selectedFeelingLiveData

//    fun saveSelectedFriends(selected: List<DocumentReference>)


}