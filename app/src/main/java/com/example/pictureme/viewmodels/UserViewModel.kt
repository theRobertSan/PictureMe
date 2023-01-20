package com.example.pictureme.viewmodels

import android.net.Uri
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

    private val _friendRequestResponseLiveData = MutableLiveData<String?>()
    val friendRequestResponseLiveData: MutableLiveData<String?> = _friendRequestResponseLiveData

    private val currentUserId = authRepository.currentUser!!.uid

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

        val currentUser = _userLiveData.value!!
        val userFriendships = currentUser.friendships
        val userFriendRequests = currentUser.friendRequests
        var failure = false

        // Check if the user is already friends with the other user
        for (friendship in userFriendships) {
            if (friendship.friend!!.username == username) {
                _friendRequestResponseLiveData.postValue("Failure: You are already friends with " + username)
                failure = true
                break
            }
        }

        // Check if the user already has a friend request from the other user
        for (request in userFriendRequests) {
            if (failure || request.sendingUser!!.username == username) {
                _friendRequestResponseLiveData.postValue("Failure: The user " + username + " already sent you a friend request")
                failure = true
                break
            }
        }

        // Send request to database
        if (!failure) {
            val databaseResponse =
                userRepository.createFriendRequest(username, _userLiveData.value!!.id!!)
            _friendRequestResponseLiveData.postValue(databaseResponse)
        }
    }

    fun handleFriendRequestAnswer(requestId: String, accepted: Boolean) = viewModelScope.launch {
        val friendship = userRepository.handleFriendRequestAnswer(requestId, accepted)

        val currentUser = _userLiveData.value!!
        currentUser.friendRequests = currentUser.friendRequests.filter { it.id != requestId }

        if (accepted) {
            val newFriendships = currentUser.friendships.toMutableList()
            newFriendships.add(friendship!!)
            currentUser.friendships = newFriendships
        }
        _userLiveData.postValue(currentUser)
    }

    fun updateProfile(fullName: String, imageUri: Uri?) = viewModelScope.launch {

        // Save to Firebase Storage
        val updatedUser = _userLiveData.value!!

        // Save to Firestore
        if (imageUri != null) {
            updatedUser.profilePicturePath =
                userRepository.storeProfileImage(currentUserId, imageUri!!)
            userRepository.updateUserProfilePicture(currentUserId, updatedUser.profilePicturePath!!)
        }

        if (updatedUser.fullName != fullName) {
            updatedUser.fullName = fullName
            userRepository.updateUserProfileFullName(currentUserId, updatedUser.fullName!!)
        }

        // Add created picme to current picme list
        _userLiveData.postValue(updatedUser)
    }

    fun resetfriendRequestResponseLiveData() {
        _friendRequestResponseLiveData.value = null
    }

}