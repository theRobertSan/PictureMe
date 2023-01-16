package com.example.pictureme.network

import com.example.pictureme.api.DistanceMatrix
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

object ApiClient {

    private val DISTANCE_MAPS_URL =
        "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DISTANCE_MAPS_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}

interface ApiService {

    @GET("/")
    fun getDistance(
        @Query("origins") origin: String,
        @Query("destinations") destinations: String,
        @Query("key") key: String
    ): Call<DistanceMatrix>

}