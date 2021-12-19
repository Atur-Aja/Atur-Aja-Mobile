//package com.example.aturaja.model
//
//import android.os.Parcelable
//import com.google.gson.annotations.SerializedName
//import kotlinx.android.parcel.Parcelize
//import kotlinx.parcelize.RawValue
//
//@Parcelize
//data class GetAllScheduleResponse(
//
//	@field:SerializedName("schedules")
//	val schedules: List<SchedulesItem>
//) : Parcelable
//
//@Parcelize
//data class Schedule2(
//
//	@field:SerializedName("date")
//	val date: String? = null,
//
//	@field:SerializedName("start_time")
//	val startTime: String? = null,
//
//	@field:SerializedName("notification")
//	val notification: String? = null,
//
//	@field:SerializedName("updated_at")
//	val updatedAt: String? = null,
//
//	@field:SerializedName("repeat")
//	val repeat: String? = null,
//
//	@field:SerializedName("end_time")
//	val endTime: String? = null,
//
//	@field:SerializedName("description")
//	val description: String? = null,
//
//	@field:SerializedName("created_at")
//	val createdAt: String? = null,
//
//	@field:SerializedName("pivot")
//	val pivot: PivotSchedule2? = null,
//
//	@field:SerializedName("location")
//	val location: String? = null,
//
//	@field:SerializedName("id")
//	val id: Int? = null,
//
//	@field:SerializedName("title")
//	val title: String? = null
//) : Parcelable
//
//@Parcelize
//data class SchedulesItem(
//
//	@field:SerializedName("schedule")
//	val schedule: Schedule2? = null,
//
//	@field:SerializedName("member")
//	val member: List<Int>? = null
//) : Parcelable
//
//@Parcelize
//data class PivotSchedule2(
//
//	@field:SerializedName("user_id")
//	val userId: Int? = null,
//
//	@field:SerializedName("schedule_id")
//	val scheduleId: Int? = null
//) : Parcelable
//
//@Parcelize
//data class MemberSchedule(
//	@field: SerializedName("member")
//	val member: ArrayList<MemberScheduleData>
//) : Parcelable
//
//@Parcelize
//data class MemberScheduleData(
//	@field:SerializedName("id")
//	val userId: Int? = null,
//
//	@field:SerializedName("username")
//	val username: String? = null,
//
//	@field:SerializedName("photo")
//	val photo: @RawValue Any? = null,
//): Parcelable
//
//@Parcelize
//data class  PivotMemberScchedule(
//	@field:SerializedName("schedule_id")
//	val scheduleId: Int? = null,
//
//	@field:SerializedName("user_id")
//	val userId: Int? = null
//): Parcelable
