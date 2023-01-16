package com.example.pictureme.api

import android.annotation.SuppressLint
import android.content.Context
import com.example.pictureme.R
import com.example.pictureme.data.utils.await
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object Maps {

//    @SuppressLint("MissingPermission")
//    fun getDistanceFromCurrent(picmeLocation: GeoPoint, context: Context): String {
//        GlobalScope.launch(Dispatchers.IO) {
//            val locationProviderClient: FusedLocationProviderClient =
//                LocationServices.getFusedLocationProviderClient(context)
//
//            val location = locationProviderClient.lastLocation.await()
//
//            val currentLocation = "${location.latitude},${location.longitude}"
//            val destinationLocation = "${picmeLocation.latitude},${picmeLocation.longitude}"
//
//            val apiKey = context.getString(R.string.MAPS_API_KEY)
//
//            val url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&" +
//                    "origins=$currentLocation&destinations=$destinationLocation&key=$apiKey"
//
//            val client = OkHttpClient()
//            val request = Request.Builder()
//                .url(url)
//                .build()
//            println(url)
//            val response = client.newCall(request).execute()
//            val json = JSONObject(response.body?.string())
//            println(json)
//            val locationString = json.getJSONArray("rows")
//                .getJSONObject(0)
//                .getJSONArray("elements")
//                .getJSONObject(0)
//                .getJSONObject("distance")
//                .getString("text")
//
//            return@launch locationString;
//        }
//    }

}