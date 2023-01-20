package com.example.pictureme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pictureme.network.DistanceMatrix
import com.example.pictureme.data.interfaces.DistanceRepository
import com.example.pictureme.data.models.Picme
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

    private var _orderedPicmesLiveData = MutableLiveData<List<Picme>?>()
    val orderedPicmesLiveData: LiveData<List<Picme>?> = _orderedPicmesLiveData

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
            }

        })
    }

    fun getDistanceOrderedPicmes(
        origin: String,
        picmes: List<Picme>,
        foodPicmes: Boolean,
        key: String
    ) =
        viewModelScope.launch {
            val selectedPicmes = ArrayList<Picme>()
            var destinations = ""
            for (picme in picmes) {
                if (picme.feeling!!.isFoodPic == foodPicmes) {
                    destinations =
                        destinations.plus("${picme.location!!.latitude},${picme.location!!.longitude}|")
                    selectedPicmes.add(picme)
                }
            }
            destinations = destinations.dropLast(1)

            if (selectedPicmes.isNotEmpty()) {
                val client = distanceRepository.getDistanceTo(origin, destinations, key)
                client.enqueue(object : Callback<DistanceMatrix> {
                    override fun onResponse(
                        call: Call<DistanceMatrix>,
                        response: Response<DistanceMatrix>
                    ) {
                        if (response.isSuccessful) {
                            val pairs = selectedPicmes.zip(response.body()!!.rows[0].elements)
                            val sortedPairs = pairs.sortedBy { it.second.distance.value }
                            val sortedPicmes = sortedPairs.unzip().first
                            _orderedPicmesLiveData.postValue(sortedPicmes)
                        }
                    }

                    override fun onFailure(call: Call<DistanceMatrix>, t: Throwable) {
                    }

                })

            }
        }


}