package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class Schedules (
    val id: Long,
    val title: String,
    val description: Any? = null,
    val location: Any? = null,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("end_time")
    val endTime: String,

    val notification: Any? = null,
    val repeat: Any? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    val pivot: PivotSchedule
)