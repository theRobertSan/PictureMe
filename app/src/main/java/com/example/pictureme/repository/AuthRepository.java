package com.example.pictureme.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.pictureme.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {

    // Firebase user
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    // Firestore user
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Boolean> loggedInMutableLiveData;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference userCollection;

    public AuthRepository() {
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        loggedInMutableLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("users");

        if (auth.getCurrentUser() != null) {
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
        }
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedInMutableLiveData() {
        return loggedInMutableLiveData;
    }

    public void register(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(task -> {
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                    loggedInMutableLiveData.postValue(true);

                    // Save user to firestore
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    userCollection
                            .document(auth.getUid())
                            .set(user)
                            .addOnCompleteListener(task2 -> {
                                userMutableLiveData.postValue(new User(username));
                            });

                })
                .addOnFailureListener(e -> {
                    System.out.println("ERROR: " + e.getMessage());
                    loggedInMutableLiveData.postValue(false);
                    userCollection
                            .document(auth.getUid())
                            .get()
                            .addOnSuccessListener(task -> {
                                userMutableLiveData.postValue(task.toObject(User.class));
                            });
                });
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(task -> {
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                    loggedInMutableLiveData.postValue(true);

                })
                .addOnFailureListener(e -> {
                    loggedInMutableLiveData.postValue(false);
                });
    }

    public void signOut() {
        auth.signOut();
        loggedInMutableLiveData.postValue(false);
    }


//    public void login(String email, String password) {
//        userCollection
//                .document(email)
//                .get()
//                .addOnCompleteListener(task -> {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists() && document.getString("password").equals(password)) {
//                        userMutableLiveData.setValue(document.toObject(User.class));
//                        loggedInMutableLiveData.setValue(true);
//                    } else {
//                        loggedInMutableLiveData.setValue(false);
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    System.out.println("Error logging in user");
//                });
//    }
//
//    public void register(String username, String email, String password) {
//        Map<String, Object> user = new HashMap<>();
//        user.put("username", username);
//        user.put("password", password);
//
//        // Check if document exists already
//        userCollection
//                .document(email)
//                .get()
//                .addOnCompleteListener(task -> {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        System.out.println("User already exists");
//                        loggedInMutableLiveData.setValue(false);
//                        return;
//                    }
//
//                    // If not, create it
//                    userCollection
//                            .document(email)
//                            .set(user)
//                            .addOnCompleteListener(task2 -> {
//                                userMutableLiveData.setValue(new User(email, username, password));
//                                loggedInMutableLiveData.setValue(true);
//                            })
//                            .addOnFailureListener(e -> {
//                                System.out.println("Error creating user");
//                            });
//
//                });
//
//
//    }

}
