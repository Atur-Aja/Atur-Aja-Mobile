package com.example.aturaja.network

import com.example.aturaja.model.*
import retrofit2.Call
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("auth/register")
    fun createUser(
        @Field("email") email:String,
        @Field("username") username:String,
        @Field("phone_number") phoneNumber:String,
        @Field("password") password:String,
        @Field("password_validate") passwordValidate:String,
    ):Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun loginUser(
        @Field("login") email:String,
        @Field("password") password:String
    ):Call<LoginResponse>

    @GET("user/schedules")
    fun getSchedules():Call<GetAllScheduleResponse>

    @GET("user/tasks")
    fun getTask():Call<GetTaskResponse>

    @FormUrlEncoded
    @POST("schedules")
    fun createSchedules(
        @Field("title") title:String,
        @Field("description") description:String ?= null,
        @Field("location") location:String ?= null,
        @Field("date") date:String,
        @Field("start_time") startTime:String,
        @Field("end_time") endTime:String,
        @Field("notification") notification:String ?= null,
        @Field("repeat") repeat:String ?= null,
        @Field("friends[]") friends:ArrayList<Int> ?= null
    ):Call<CreateScheduleResponse>

    @FormUrlEncoded
    @PUT("schedules/{id}")
    fun updateSchedule(
        @Path("id") id:Int,
        @Field("title") title:String,
        @Field("description") description:String ?= null,
        @Field("location") location:String ?= null,
        @Field("date") date:String,
        @Field("start_time") startTime:String,
        @Field("end_time") endTime:String,
        @Field("notification") notification:String ?= null,
        @Field("repeat") repeat:String ?= null,
        @Field("friends[]") friends:ArrayList<Int> ?= null
    ):Call<UpdateScheduleResponse>

    @FormUrlEncoded
    @POST("schedules/{id}")
    fun deleteSchedule(
        @Path("id") id:Int,
        @Field("_method") methode:String = "DELETE"
    ):Call <DeleteScheduleResponse>

    @FormUrlEncoded
    @POST("tasks/")
    fun createTask(
        @Field("title") title:String,
        @Field("date") date:String,
        @Field("time") time:String,
        @Field("todos[]") todos:ArrayList<String>
    ):Call<AddTaskResponse>

    @FormUrlEncoded
    @PUT("tasks/{id}")
    fun updateTask(
        @Path("id") id:Int,
        @Field("title") title:String,
        @Field("date") date:String,
        @Field("time") time:String
    ):Call<UpdateTaskResponse>

    @DELETE("tasks/{id}")
    fun deleteTask(
        @Path("id") id:Int,
    ):Call <DeleteTaskResponse>

    @FormUrlEncoded
    @POST("todos")
    fun createTodo(
        @Field("task_id") id:Int,
        @Field("todos") todos: List<String>
    ):Call<AddTodoResponse>

    @GET("user/friends")
    fun getFriends():Call<List<GetFriendResponse>>
}