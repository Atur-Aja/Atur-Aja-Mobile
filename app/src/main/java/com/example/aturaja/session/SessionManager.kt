package com.example.aturaja.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.aturaja.R

//class SessionManager private constructor(private val mCtx: Context) {
//    companion object {
//        const val USER_TOKEN = "user_token"
//        private var mInstance: SessionManager? = null
//        @Synchronized
//        fun getInstance(mCtx: Context): SessionManager {
//            if (mInstance == null) {
//                mInstance = SessionManager(mCtx)
//            }
//            return mInstance as SessionManager
//        }
//    }
//
//    fun isLoggedIn(): Boolean {
//        val sharedPreferences = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
//
//        return sharedPreferences.getString("userToken", "no") != "no"
//    }
//
//    fun saveToken(token: String?) {
//        val sharedPreferences = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        editor.putString("userToken", token)
//        editor.apply()
//    }
//
//    fun clearToken() {
//        val sharedPreferences = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        editor.clear()
//        editor.apply()
//    }
//
//    fun fetchAuthToken(): String? {
//        val token = mCtx.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE)
//        val token2 = token.getString("userToken", null)
//
//        return token2
//    }
//}

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "username"
        const val SERVICE_STATUS = "servis_status"
    }

    fun isLoggedIn(): Boolean {
        return prefs.getString(USER_TOKEN, "no") != "no"
    }

    fun clearTokenAndUsername() {
        val editor = prefs.edit()

        editor.clear()
        editor.apply()
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveUsername(username: String) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, username)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUsername(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun clearStatusService() {
        val editor = prefs.edit()

        editor.clear()
        editor.apply()
    }

    /**
     * Function to save auth token
     */
    fun saveStatusService(status: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(SERVICE_STATUS, status)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */

    fun fetchStatusService(): Boolean {
        return prefs.getBoolean(SERVICE_STATUS, false)
    }
}