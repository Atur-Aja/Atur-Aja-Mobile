package com.aturaja.aturaja.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("message")
	val message: String
)
