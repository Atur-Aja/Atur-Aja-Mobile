package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class PivotTask(@SerializedName("user_id")
                     val userID: Long,

                     @SerializedName("task_id")
                     val taskID: Long)
