package com.example.pictureme.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FilterPicmes {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getFilteredPicmes(
        picmes: List<Picme>,
        foodFeelings: List<Feeling>
    ): ArrayList<Pair<String, ArrayList<Picme>>> {
        var picmesWithFriends = ArrayList<Picme>()
        var picmesFood = ArrayList<Picme>()
        var picmesLast24Hours = ArrayList<Picme>()

        var filters = ArrayList<Pair<String, ArrayList<Picme>>>()

        for (picme in picmes) {
            val picmeCreatedAt = Details.getRelativeDate(picme.createdAt!!).split(" ")
            if(picmeCreatedAt[1].equals("hours") && picmeCreatedAt[0].toInt() <= 24){
                picmesLast24Hours.add(picme)
            }
            if (picme.friends.isNotEmpty()) {
                picmesWithFriends.add(picme)
            }
            if (picme.feeling?.isFoodPic == true) {
                picmesFood.add(picme)
            }
        };
        var last24hoursPair = Pair("Last 24 hours", picmesLast24Hours)
        var friendsPair = Pair("Your PicMe's with friends", picmesWithFriends)
        var foodPair = Pair("Food PicMes", picmesFood)
        filters.add(last24hoursPair)
        filters.add(friendsPair)
        filters.add(foodPair)
        return filters
    }

    fun getNumPicmesWithEachFriend (
        picmes: List<Picme>,
        friendsIds: List<String>
    ): HashMap<String, Int> {
        val friendNumberPicmes : HashMap<String, Int> = HashMap<String, Int>(friendsIds.size)
        for( friendId in friendsIds ) {
            friendNumberPicmes[friendId] = 0
        }

        for ( picme in picmes) {
            for( friendId in friendsIds) {
                if (picme.friends.find{ it.id == friendId } != null) {
                    val oldNumber = friendNumberPicmes[friendId]
                    if (oldNumber != null) {
                        friendNumberPicmes[friendId] = oldNumber + 1
                    }
                }
            }
        }
        return friendNumberPicmes
    }

}