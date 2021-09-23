package com.example.aturaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ForgotPasswordConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_confirm)
    }

    fun emailConfirmOnClick(view: android.view.View) {

    }
    fun backOnClick(view: android.view.View) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}