package com.aturaja.aturaja.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GetAllTaskResponse2(

	@field:SerializedName("tasks")
	val tasks: List<TasksItem>? = null
) : Parcelable

@Parcelize
data class Task(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("status")
	var status: Int? = null
) : Parcelable

@Parcelize
data class PivotTask2(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("task_id")
	val taskId: Int? = null
) : Parcelable

@Parcelize
data class TodoItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("task_id")
	val taskId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("update_by")
	val updateBy: @RawValue  Any? = null,

	@field:SerializedName("status")
	var status: Int? = null
) : Parcelable

@Parcelize
data class MemberItem(

	@field:SerializedName("photo")
	val photo: @RawValue Any? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable

@Parcelize
data class TasksItem(

	@field:SerializedName("todo")
	val todo: List<TodoItem?>? = null,

	@field:SerializedName("task")
	val task: Task? = null,

	@field:SerializedName("member")
	val member: List<MemberItem?>? = null
) : Parcelable
