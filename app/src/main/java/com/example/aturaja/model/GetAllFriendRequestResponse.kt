package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class GetAllFriendRequestResponse(
	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("photo")
	val photo: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: PivotFriendRequest? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullname")
	val fullname: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class PivotFriendRequest(

	@field:SerializedName("second_user_id")
	val secondUserId: Int? = null,

	@field:SerializedName("first_user_id")
	val firstUserId: Int? = null
)
