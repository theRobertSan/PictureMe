package com.example.pictureme.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.R
import com.example.pictureme.data.interfaces.AuthRepository
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.PreviewPicme
import com.example.pictureme.data.utils.await
import com.example.pictureme.utils.Details
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

class PicmeDetailsViewModel(
) : ViewModel() {
    // Preview Picme
    private val _picmeLiveData = MutableLiveData<Picme>()
    val picmeLiveData: LiveData<Picme> = _picmeLiveData

    // Relative Location
    private val _relativeLocationLiveData = MutableLiveData<String>()
    val relativeLocationLiveData: LiveData<String> = _relativeLocationLiveData

    fun selectPicme(picme: Picme) = viewModelScope.launch {
        _picmeLiveData.postValue(picme)
    }

}