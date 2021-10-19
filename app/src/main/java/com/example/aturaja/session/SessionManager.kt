package com.example.aturaja.session

import android.content.Context
import android.content.SharedPreferences
import com.example.aturaja.R

class SessionManager private constructor(private val mCtx: Context) {
    companion object {
        const val USER_TOKEN = "user_token"
        private var mInstance: SessionManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SessionManager {
            if (mInstance == null) {
                mInstance = SessionManager(mCtx)
            }
            return mInstance as SessionManager
        }
    }

    fun isLoggedIn(): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)

        return sharedPreferences.getString("userToken", "no") != "no"
    }

    fun saveToken(token: String?) {
        val sharedPreferences = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("userToken", token)
        editor.apply()
    }

    fun clearToken() {
        val sharedPreferences = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.clear()
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        val token = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
        val token2 = token.getString("userToken", null)

        return token2
    }
}