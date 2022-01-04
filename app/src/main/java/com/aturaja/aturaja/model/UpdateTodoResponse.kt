package com.aturaja.aturaja.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateTodoResponse(

	@field:SerializedName("test")
	val test: Test? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class Test(

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
	val status: Boolean? = null
) : Parcelable
