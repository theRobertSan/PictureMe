package com.example.pictureme.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
import com.example.pictureme.data.models.User
import com.example.pictureme.viewmodels.FilteredPicmesViewModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FilterPicmes {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getFilteredPicmes(
        picmes: List<Picme>,
        foodFeelings: List<Feeling>
    ): List<Pair<String, ArrayList<Picme>>> {
        val picmesWithFriends = ArrayList<Picme>()
        val picmesFood = ArrayList<Picme>()
        val picmesLast24Hours = ArrayList<Picme>()

        val filters = ArrayList<Pair<String, ArrayList<Picme>>>()

        for (picme in picmes) {
            val picmeCreatedAt = Details.getRelativeDate(picme.createdAt!!).split(" ")
            if(picmeCreatedAt[1].equals("hours") && picmeCreatedAt[0].toInt() <= 24){
                picmesLast24Hours.add(picme)
            }else if(picmeCreatedAt[1].equals("minutes") && picmeCreatedAt[0].toInt() <= 60){
                picmesLast24Hours.add(picme)
            }else if(picmeCreatedAt[1].equals("seconds") && picmeCreatedAt[0].toInt() <= 60){
                picmesLast24Hours.add(picme)
            }
            if (picme.friends.isNotEmpty()) {
                picmesWithFriends.add(picme)
            }
            if (picme.feeling?.isFoodPic == true) {
                picmesFood.add(picme)
            }
        }

        if(picmesLast24Hours.isNotEmpty()) {
            filters.add(Pair("Last 24 hours", picmesLast24Hours))
        }
        if(picmesWithFriends.isNotEmpty()) {
            filters.add(Pair("Your PicMe's with friends", picmesWithFriends))
        }
        if(picmesFood.isNotEmpty()) {
            filters.add(Pair("Food PicMes", picmesFood))
        }

        return filters.shuffled()
    }

    fun filterFoodPicmes(picmes: List<Picme>): ArrayList<Picme>{
        var picmesFood = ArrayList<Picme>()
        for (picme in picmes) {
            if (picme.feeling?.isFoodPic!!) {
                picmesFood.add(picme)
            }
        }
        return picmesFood
    }
    fun filterOldestPicmes(picmes: List<Picme>): List<Picme>{
        return picmes.sortedByDescending { it.createdAt!!.seconds }
    }
    fun filterNewestPicmes(picmes: List<Picme>): List<Picme>{
        return picmes.sortedBy { it.createdAt!!.seconds }
    }
    fun filterFriendPicmes(picmes: List<Picme>, friend: User): ArrayList<Picme>{
        var picmesWFriend = ArrayList<Picme>()
        for (picme in picmes){
            if(picme.friends.contains(friend)){
                picmesWFriend.add(picme)
            }
        }
        return picmesWFriend
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