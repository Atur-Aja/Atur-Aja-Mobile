package com.aturaja.aturaja.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import com.google.gson.Gson




class APIClient {
    private lateinit var apiService: API
    private val BASE_URL = "http://192.168.1.66:8000/api/"
//    https://api.aturaja.me/api/
//    http://192.168.1.66:8000/api/

    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getApiService(context: Context): API {

        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(API::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}