package com.aturaja.aturaja.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class GetAllScheduleResponse2(

	@field:SerializedName("schedules")
	val schedules: List<SchedulesItem>
): Parcelable

@Parcelize
data class MemberItemSchedules(

	@field:SerializedName("photo")
	val photo: @RawValue Any? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
): Parcelable

@Parcelize
data class Pivot(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("schedule_id")
	val scheduleId: Int? = null
): Parcelable

@Parcelize
data class SchedulesItem(

	@field:SerializedName("schedule")
	val schedule: ScheduleNew? = null,

	@field:SerializedName("member")
	val member: List<MemberItemSchedules?>? = null
): Parcelable

@Parcelize
data class ScheduleNew(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("notification")
	val notification: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("repeat")
	val repeat: String? = null,

	@field:SerializedName("end_time")
	val endTime: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
): Parcelable
