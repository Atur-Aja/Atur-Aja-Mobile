package com.example.aturaja.network

import com.example.aturaja.model.LoginResponse
import com.example.aturaja.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("register")
    fun createUser(
        @Field("username") username:String,
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("password_validate") passwordValidate:String,
        @Field("phone_number") phoneNumber:String
    ):Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<LoginResponse>
}