package com.example.pictureme.data.repository

import com.example.pictureme.api.DistanceMatrix
import com.example.pictureme.data.interfaces.DistanceRepository
import com.example.pictureme.network.ApiClient
import com.example.pictureme.network.ApiService
import retrofit2.Call
import javax.inject.Inject

class DistanceRepositoryImpl @Inject constructor() : DistanceRepository {
    override suspend fun getDistanceTo(
        origin: String,
        destination: String,
        key: String
    ): Call<DistanceMatrix> {
        return ApiClient.apiService.getDistance(origin, destination, key)
    }
}