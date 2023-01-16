package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.interfaces.UserRepository
import com.example.pictureme.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> = _userLiveData

    fun addUser(username: String, fullName: String) = viewModelScope.launch {
        // Add user to firestore
        val user = userRepository.addUser(authRepository.currentUser!!.uid, username, fullName)
        // Post it
        _userLiveData.postValue(user)
    }

    fun loadUser() = viewModelScope.launch {
        val user = userRepository.loadUser(authRepository.currentUser!!.uid)
        _userLiveData.postValue(user)
    }

    fun sendFriendRequest(username: String) = viewModelScope.launch {
        userRepository.createFriendRequest(username, _userLiveData.value!!.id!!)
    }

}