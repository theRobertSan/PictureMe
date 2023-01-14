package com.example.pictureme.utils

import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Picme

object FilterPicmes {
    fun getFilteredPicmes(
        picmes: List<Picme>,
        foodFeelings: List<Feeling>
    ): ArrayList<Pair<String, ArrayList<Picme>>> {
        var picmesWithFriends = ArrayList<Picme>()
        var picmesFood = ArrayList<Picme>()
        var filters = ArrayList<Pair<String, ArrayList<Picme>>>()
        for (picme in picmes) {
            if (picme.friends.size > 1) {
                picmesWithFriends.add(picme)
            }
            if (picme.feeling?.isFoodPic == true) {
                picmesFood.add(picme)
            }
        };
        var friendsPair = Pair("Your PicMe's with friends", picmesWithFriends)
        var foodPair = Pair("Food PicMes", picmesFood)
        filters.add(friendsPair)
        filters.add(foodPair)
        return filters
    }
}