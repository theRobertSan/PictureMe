package com.example.pictureme.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

object Details {

    fun getExactDate(timestamp: Timestamp): String {
        val formatter = SimpleDateFormat("dd MMM yyyy")
        return formatter.format(timestamp.toDate())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRelativeDate(timestamp: Timestamp): String {
        val duration = Duration.between(timestamp.toDate().toInstant(), Instant.now())

        val seconds = duration.seconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = days / 365

        val relativeTime = when {
            years > 0 -> "$years years ago"
            months > 0 -> "$months months ago"
            days > 0 -> "$days days ago"
            hours > 0 -> "$hours hours ago"
            minutes > 0 -> "$minutes minutes ago"
            else -> "$seconds seconds ago"
        }

        return relativeTime
    }

    fun getExactLocation(location: GeoPoint, context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)

        val city = addresses[0].locality
        val country = addresses[0].countryName

        return "$city, $country"
    }

//    @SuppressLint("MissingPermission")
//    suspend fun getRelativeLocation(picmeLocation: GeoPoint, context: Context): String {
//
//    }
}