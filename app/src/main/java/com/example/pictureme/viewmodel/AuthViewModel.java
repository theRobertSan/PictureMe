package com.example.pictureme.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pictureme.models.User;
import com.example.pictureme.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private AuthRepository userRepository;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private MutableLiveData<User> user;
    private MutableLiveData<Boolean> loggedIn;

    public AuthViewModel() {
        userRepository = new AuthRepository();
        firebaseUser = userRepository.getFirebaseUserMutableLiveData();
        user = userRepository.getUserMutableLiveData();
        loggedIn = userRepository.getLoggedInMutableLiveData();
    }

    public LiveData<FirebaseUser> getFirebaseUserLiveData() {
        return firebaseUser;
    }

    public LiveData<User> getUserLiveData() {
        return user;
    }

    public LiveData<Boolean> getLoggedInLiveData() {
        return loggedIn;
    }

    public void register(String email, String password, String username) {
        userRepository.register(email, password, username);
    }

    public void login(String email, String password) {
        userRepository.login(email, password);
    }

    public void signOut() {
        userRepository.signOut();
    }

    public boolean matchingPasswords(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

}
