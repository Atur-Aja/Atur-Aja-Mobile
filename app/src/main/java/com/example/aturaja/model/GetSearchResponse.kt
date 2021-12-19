package com.example.aturaja.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GetSearchResponse(

	@field:SerializedName("GetSearchResponse")
	val getSearchResponse: List<GetSearchResponseItem>? = null
) : Parcelable

@Parcelize
data class GetSearchResponseItem(

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("photo")
	val photo: @RawValue Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
