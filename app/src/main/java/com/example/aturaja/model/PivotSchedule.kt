package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class PivotSchedule (
    @SerializedName("user_id")
    val userID: Long,

    @SerializedName("schedule_id")
    val scheduleID: Long
)