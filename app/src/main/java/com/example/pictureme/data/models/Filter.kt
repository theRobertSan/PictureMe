package com.example.pictureme.data.models

data class Filter(
    var sortBy: Int? = null,
    var friendIds: ArrayList<String> = ArrayList()
)