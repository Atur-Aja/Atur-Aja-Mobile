package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

//data class GetScheduleResponse(
//	@field:SerializedName("id")
//	val id: Int? = null,
//
//	@field:SerializedName("title")
//	val title: String? = null,
//
//	@field:SerializedName("description")
//	val description: Any? = null,
//
//	@field:SerializedName("location")
//	val location: Any? = null,
//
//	@field:SerializedName("start_date")
//	val startDate: String? = null,
//
//	@field:SerializedName("start_time")
//	val startTime: String? = null,
//
//	@field:SerializedName("end_date")
//	val endDate: String? = null,
//
//	@field:SerializedName("end_time")
//	val endTime: String? = null,
//
//	@field:SerializedName("notification")
//	val notification: Any? = null,
//
//	@field:SerializedName("repeat")
//	val repeat: Any? = null,
//
//	@field:SerializedName("user_id")
//	val userId: Int? = null,
//
//	@field:SerializedName("created_at")
//	val createdAt: String? = null,
//
//	@field:SerializedName("updated_at")
//	val updatedAt: String? = null,
//)

data class GetScheduleResponse(
	val schedules: List<ScheduleElement>
)
