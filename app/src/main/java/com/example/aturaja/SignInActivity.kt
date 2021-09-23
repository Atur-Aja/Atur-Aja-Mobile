package com.example.aturaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    fun signUpOnClick(view: android.view.View) {

    }
    fun forgotPasswordOnClick(view: android.view.View) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)

    }
}