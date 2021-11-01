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
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<LoginResponse>

    @GET("user/{username}/schedules")
    fun getSchedules(
        @Path("username") username: String
    ):Call<ArrayList<GetScheduleResponse>>

    @FormUrlEncoded
    @POST("schedules")
    fun createSchedules(
        @Field("title") title:String,
        @Field("start_date") startDate:String,
        @Field("start_time") startTime:String,
        @Field("end_date") endDate:String,
        @Field("end_time") endTime:String
    ):Call<CreateScheduleResponse>

    @FormUrlEncoded
    @POST("schedules/{id}")
    fun updateSchedule(
        @Path("id") id:Int,
        @Field("title") title:String,
        @Field("start_date") startDate:String,
        @Field("start_time") startTime:String,
        @Field("end_date") endDate:String,
        @Field("end_time") endTime:String,
        @Field("_method") methode:String = "PUT"
    ):Call<UpdateScheduleResponse>

    @FormUrlEncoded
    @POST("schedules/{id}")
    fun deleteSchedule(
        @Path("id") id:Int,
        @Field("_method") methode:String = "DELETE"
    ):Call <DeleteScheduleResponse>
}