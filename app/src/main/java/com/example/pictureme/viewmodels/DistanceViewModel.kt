package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.api.DistanceMatrix
import com.example.pictureme.data.interfaces.DistanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class DistanceViewModel @Inject constructor(
    private val distanceRepository: DistanceRepository
) : ViewModel() {

    private var _distanceLiveData = MutableLiveData<String>()
    val distanceLiveData: LiveData<String> = _distanceLiveData

    fun getDistanceTo(origin: String, destination: String, key: String) = viewModelScope.launch {
        val client = distanceRepository.getDistanceTo(origin, destination, key)
        client.enqueue(object : Callback<DistanceMatrix> {
            override fun onResponse(
                call: Call<DistanceMatrix>,
                response: Response<DistanceMatrix>
            ) {
                if (response.isSuccessful) {
                    _distanceLiveData.postValue(response.body()!!.rows[0].elements[0].distance.text)
                }
            }

            override fun onFailure(call: Call<DistanceMatrix>, t: Throwable) {
                println("Bruh")
            }

        })
    }

}