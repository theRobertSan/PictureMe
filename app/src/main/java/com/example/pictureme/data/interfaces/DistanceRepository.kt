package com.example.pictureme.data.interfaces

import com.example.pictureme.api.DistanceMatrix
import retrofit2.Call

interface DistanceRepository {
    suspend fun getDistanceTo(
        origin: String,
        destination: String,
        key: String
    ): Call<DistanceMatrix>
}