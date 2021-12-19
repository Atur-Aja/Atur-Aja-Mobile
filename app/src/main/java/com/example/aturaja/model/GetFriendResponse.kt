package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class GetFriendResponse(

	val id: Long,
	val username: String,
	val email: String,
	val fullname: Any? = null,

	@SerializedName("email_verified_at")
	val emailVerifiedAt: Any? = null,

	val photo: Any? = null,

	@SerializedName("phone_number")
	val phoneNumber: Any? = null,

	@SerializedName("created_at")
	val createdAt: String,

	@SerializedName("updated_at")
	val updatedAt: String,

	val pivot: PivotFriends
)

data class PivotFriends(

	@field:SerializedName("second_user_id")
	val secondUserId: Int? = null,

	@field:SerializedName("first_user_id")
	val firstUserId: Int? = null
)
