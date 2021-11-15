package com.example.aturaja.network

import com.example.aturaja.model.AddTaskResponse
import com.example.aturaja.model.GetTaskResponse
import retrofit2.Call
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("tasks/")
    fun addTask(
        @Field("title") title:String,
        @Field("description") description:String,
        @Field("date") date:String,
        @Field("time") time:String,
        @Field("todos") todos:List<String>
    ):Call<AddTaskResponse>

    @GET("user/tasks")
    fun getTask():Call<GetTaskResponse>

}