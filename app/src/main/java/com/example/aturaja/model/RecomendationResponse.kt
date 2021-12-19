package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class RecomendationResponse(

	@field:SerializedName("rekomendasi")
	val rekomendasi: List<RekomendasiItem>
)

data class RekomendasiItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("start_time")
	val startTime: String,

	@field:SerializedName("end_time")
	val endTime: String
)
