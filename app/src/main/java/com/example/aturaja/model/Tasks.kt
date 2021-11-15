package com.example.aturaja.model

import com.google.gson.annotations.SerializedName

data class Tasks(val id: Long,
                 val title: String,
                 val description: Any? = null,
                 val date: String,
                 val time: String,
                 val status: Any? = null,

                 @SerializedName("created_at")
                 val createdAt: String,

                 @SerializedName("updated_at")
                 val updatedAt: String,

                 val pivot: PivotTask)
