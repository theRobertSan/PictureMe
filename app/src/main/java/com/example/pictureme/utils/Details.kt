package com.example.pictureme.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


object Details {

//    fun getExactDate(timestamp: Timestamp): String {
//        val format = DateTimeFormatter.ofPattern("dd MMM yyyy")
//        return LocalDate.parse(timestamp.toDate().toString(), format).toString()
//    }

    fun getExactLocation(location: GeoPoint, context: Context): String {
        println("-------------------")
        println(location)
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val cityName: String = addresses[0].getAddressLine(0)

        return "$cityName"
    }

}