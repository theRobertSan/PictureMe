package com.example.pictureme.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme
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
        val picmesRandom = ArrayList<Picme>()
        val picmesLongAgo = ArrayList<Picme>()
        val picmesHappy = ArrayList<Picme>()
        val picmesLove = ArrayList<Picme>()

        val filters = ArrayList<Pair<String, ArrayList<Picme>>>()

        for (picme in picmes) {
            val picmeCreatedAt = Details.getRelativeDate(picme.createdAt!!).split(" ")
            if (picmeCreatedAt[1].equals("hours") && picmeCreatedAt[0].toInt() <= 24) {
                picmesLast24Hours.add(picme)
            } else if (picmeCreatedAt[1].equals("minutes") && picmeCreatedAt[0].toInt() <= 60) {
                picmesLast24Hours.add(picme)
            } else if (picmeCreatedAt[1].equals("seconds") && picmeCreatedAt[0].toInt() <= 60) {
                picmesLast24Hours.add(picme)
            }
            if (picme.friends.isNotEmpty()) {
                picmesWithFriends.add(picme)
            }
            if (picme.feeling?.isFoodPic == true) {
                picmesFood.add(picme)
            }
            if (picmeCreatedAt[1].equals("months")) {
                picmesLongAgo.add(picme)
            } else if (picmeCreatedAt[1].equals("years")) {
                picmesLongAgo.add(picme)
            }
            val feeling = picme.feeling?.feeling
            if (feeling in listOf("Happy", "Excited")) {
                picmesHappy.add(picme)
            }
            if (feeling == "In Love") {
                picmesLove.add(picme)
            }

        }

        if (picmesLast24Hours.isNotEmpty()) {
            filters.add(Pair("Your PicMes from the last day", picmesLast24Hours))
        }
        if (picmesWithFriends.isNotEmpty()) {
            filters.add(Pair("Your PicMe's with friends", picmesWithFriends))
        }
        if (picmesFood.isNotEmpty()) {
            filters.add(Pair("Food PicMes", picmesFood))
        }
        if (picmes.isNotEmpty()) {
            filters.add(
                Pair(
                    "Some random PicMes",
                    ArrayList(picmes.shuffled().dropLast(picmes.size / 2))
                )
            )
        }

        if (picmesLongAgo.isNotEmpty()) {
            filters.add(Pair("Your PicMes from long ago...", picmesLongAgo))
        }
        if (picmesHappy.isNotEmpty()) {
            filters.add(Pair("Your uplifting PicMes", picmesHappy))
        }
        if (picmesLove.isNotEmpty()) {
            filters.add(Pair("Your romantic PicMes", picmesLove))
        }

        return filters
    }

    fun filterFoodPicmes(picmes: List<Picme>): ArrayList<Picme> {
        var picmesFood = ArrayList<Picme>()
        for (picme in picmes) {
            if (picme.feeling?.isFoodPic!!) {
                picmesFood.add(picme)
            }
        }
        return picmesFood
    }

    fun filterOldestPicmes(picmes: List<Picme>): List<Picme> {
        return picmes.sortedBy { it.createdAt!! }
    }

    fun filterNewestPicmes(picmes: List<Picme>): List<Picme> {
        return picmes.sortedByDescending { it.createdAt!! }
    }

    fun filterFriendsPicmes(picmes: List<Picme>, friends: ArrayList<String>): ArrayList<Picme> {
        var allFriendsPresent = true
        var picmesWFriend = ArrayList<Picme>()
        for (picme in picmes) {
            allFriendsPresent = true
            for (friend in friends) {
                if (!picme.friends.any { picmeFriend -> picmeFriend.id == friend }) {
                    allFriendsPresent = false
                }
            }
            if (allFriendsPresent) {
                picmesWFriend.add(picme)
            }
        }
        return picmesWFriend
    }

    fun getNumPicmesWithEachFriend(
        picmes: List<Picme>,
        friendsIds: List<String>
    ): HashMap<String, Int> {
        val friendNumberPicmes: HashMap<String, Int> = HashMap<String, Int>(friendsIds.size)
        for (friendId in friendsIds) {
            friendNumberPicmes[friendId] = 0
        }

        for (picme in picmes) {
            for (friendId in friendsIds) {
                if (picme.friends.find { it.id == friendId } != null) {
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