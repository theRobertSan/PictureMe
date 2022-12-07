package com.example.pictureme.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.pictureme.models.Picme;
import com.example.pictureme.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PicmeRepository {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private CollectionReference picmeCollection;
    private CollectionReference userPicmeCollection;
    private CollectionReference userCollection;
    private MutableLiveData<List<Picme>> picmeListMutableLiveData;

    public PicmeRepository() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        picmeCollection = db.collection("picmes");
        userPicmeCollection = db.collection("userPicmes");
        userCollection = db.collection("users");
        picmeListMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Picme>> getPicmeListMutableLiveData() {
        return picmeListMutableLiveData;
    }

    public void loadUserPicmes() {
        String uid = firebaseUser.getUid();
        userPicmeCollection
                .whereEqualTo("userId", userCollection.document(uid))
                .get()
                .addOnCompleteListener(task -> {
                    // Get references to picmes
                    List<DocumentReference> picmeReferences = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        picmeReferences.add((DocumentReference) document.get("picmeId"));
                    }

                    // Get picmes from references
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                    for (DocumentReference documentReference : picmeReferences) {
                        tasks.add(documentReference.get());
                    }
                    Tasks.whenAllSuccess(tasks)
                            .addOnSuccessListener(objects -> {
                                List<Picme> picmes = new ArrayList<>();
                                for (Object object : objects) {
                                    picmes.add(((DocumentSnapshot) object).toObject(Picme.class));
                                }
                                picmeListMutableLiveData.postValue(picmes);
                                System.out.println("---------------");
                                System.out.println(picmes.get(0).getIsFoodPic());
                            });
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error logging in user");
                });
    }

}
