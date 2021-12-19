package com.example.aturaja.model

data class AddTodoResponse(val code: Long,
                           val message: String,
                           val description: String,
                           val exception: Exception)