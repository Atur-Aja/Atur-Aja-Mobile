package com.aturaja.aturaja.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetTaskResponse(

	@field:SerializedName("task")
	val task: List<TaskItem?>? = null,

	@field:SerializedName("todos")
	val todos: List<TodosItem?>? = null
) : Parcelable

@Parcelize
data class PivotGetTaskById(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("task_id")
	val taskId: Int? = null
) : Parcelable

@Parcelize
data class TaskItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: PivotGetTaskById? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class TodosItem(

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
	val updateBy: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable
