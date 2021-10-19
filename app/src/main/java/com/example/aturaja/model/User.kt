package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("username")
    var username: String
)