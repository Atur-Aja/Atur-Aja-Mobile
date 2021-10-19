package com.example.aturaja.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String,
    @Json(name = "expires_in")
    val expiresIn: Long
)