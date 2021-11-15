package com.example.aturaja.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    //private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
//        sessionManager.fetchAuthToken()?.let {
//            requestBuilder.addHeader("Authorization", "Bearer $it")
//        }

        requestBuilder.addHeader("Authorization",
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC9hdXRoXC9sb2dpbiIsImlhdCI6MTYzNjk4NzcxMCwiZXhwIjoxNjM2OTkxMzEwLCJuYmYiOjE2MzY5ODc3MTAsImp0aSI6IjVTSTFzWlNKdU9SZnlQUnQiLCJzdWIiOjEsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.ND8_jRzxHj1wHEARXLeOMml5AM3mThSBls0oqMcE6rM")
        return chain.proceed(requestBuilder.build())
    }
}