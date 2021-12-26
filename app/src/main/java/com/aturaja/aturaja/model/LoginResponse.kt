package com.aturaja.aturaja.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("access_token")
	val accessToken: String,

	@field:SerializedName("token_type")
	val tokenType: String,

	@field:SerializedName("expires_in")
	val expiresIn: Int
)
