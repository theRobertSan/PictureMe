package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.Response
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (
    private val repository: AuthRepository
): ViewModel() {

    private val _authLiveData = MutableLiveData<Response<FirebaseUser>?>()
    val authLiveData: LiveData<Response<FirebaseUser>?> = _authLiveData

    val currentUser: FirebaseUser?
        get() = repository.currentUser

//    init {
//        if (repository.currentUser != null) {
//            _authLiveData.value = Resource.Success(repository.currentUser!!)
//        }
//    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _authLiveData.value = Response.Loading
        val result = repository.login(email, password)
        _authLiveData.value = result
    }

    fun signup(name: String, email: String, password: String) = viewModelScope.launch {
        _authLiveData.value = Response.Loading
        val result = repository.signup(name, email, password)
        _authLiveData.value = result
    }

    fun logout() {
        repository.logout()
        _authLiveData.value = null
    }

}