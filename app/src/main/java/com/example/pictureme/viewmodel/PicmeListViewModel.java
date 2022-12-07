package com.example.pictureme.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pictureme.models.Picme;
import com.example.pictureme.repository.PicmeRepository;

import java.util.List;

public class PicmeListViewModel extends ViewModel {
    MutableLiveData<List<Picme>> userPicmes;
    PicmeRepository picmeRepository;

    public PicmeListViewModel() {
        picmeRepository = new PicmeRepository();
        userPicmes = picmeRepository.getPicmeListMutableLiveData();
    }

    public LiveData<List<Picme>> getUserPicmesLiveData() {
        return userPicmes;
    }

    public void loadUserPicmes() {
        picmeRepository.loadUserPicmes();
    }


}
