package com.aturaja.aturaja.model

import com.google.gson.annotations.SerializedName

data class ChangeEmailResponse(
	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Errors? = null
)

data class Errors(

	@field:SerializedName("email")
	val email: List<String?>? = null
)
