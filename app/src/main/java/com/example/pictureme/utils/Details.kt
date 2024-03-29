package com.example.pictureme.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.pictureme.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

object Details {

    val feelingToImage = hashMapOf(
        "Excited" to R.drawable.ic_excited,
        "Sad" to R.drawable.ic_sad,
        "Happy" to R.drawable.ic_happy,
        "Shocked" to R.drawable.ic_shocked,
        "Dead" to R.drawable.ic_dead,
        "Calm" to R.drawable.ic_calm,
        "In Love" to R.drawable.ic_in_love,
        "Yummy" to R.drawable.ic_yummy,
        "Disgusting" to R.drawable.ic_disgusting,
        "Gourmet" to R.drawable.ic_star,
        "Tasty" to R.drawable.ic_looks,
        "Refreshing" to R.drawable.ic_local_cafe,
        "Junk Food" to R.drawable.ic_fast_food,
        "Bland" to R.drawable.ic_canned_food,
        "Healthy" to R.drawable.ic_broccoli,
        "Feast" to R.drawable.ic_food_dish,
        "Snack" to R.drawable.ic_muffin,
        "Steamy" to R.drawable.ic_hot_bowl,
        "Dessert" to R.drawable.ic_cake
    )

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
        val country = addresses[0].countryCode

        return "$city, $country"
    }

    fun getFeelingImage(feeling: String): Int? {
        return feelingToImage[feeling]
    }

    fun getPrettyNameFormat(fullName: String): String {
        var names = fullName.trim().split(" ")
        if (names.size == 1) {
            return fullName
        }
        return "${names.first()} ${names.last()[0]}."
    }

}