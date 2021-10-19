package com.example.aturaja.network

import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private const val BASE_URL = "http://192.168.1.66:8000/api/auth/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", "")
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    val instance: API by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .client(okHttpClient)
            .build()

        retrofit.create(API::class.java)
    }

}