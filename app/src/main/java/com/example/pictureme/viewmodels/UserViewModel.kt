package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.Resource
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.interfaces.UserRepository
import com.example.pictureme.data.models.User
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _userLiveData = MutableLiveData<Resource<User>?>()
    val userLiveData: LiveData<Resource<User>?> = _userLiveData

    private val _friendsLiveData = MutableLiveData<Resource<ArrayList<User>>?>()
    val friendsLiveData: LiveData<Resource<ArrayList<User>>?> = _friendsLiveData

    fun addUser(username: String) = viewModelScope.launch {
        // Add user to firestore
        val user = userRepository.addUser(authRepository.currentUser!!.uid, username)
        _userLiveData.value = user
        // Load its friends
//        val friends = userRepository.getFriends(authRepository.currentUser!!.uid)
//        _friendsLiveData.value = friends
    }

    fun loadUser() = viewModelScope.launch {
        _userLiveData.value = Resource.Loading
        val user = userRepository.loadUser(authRepository.currentUser!!.uid)
        _userLiveData.value = user
    }

}