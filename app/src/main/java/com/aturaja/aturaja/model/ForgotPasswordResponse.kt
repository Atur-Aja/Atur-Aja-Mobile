package com.aturaja.aturaja.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(

	@field:SerializedName("message")
	val message: String? = null
)
