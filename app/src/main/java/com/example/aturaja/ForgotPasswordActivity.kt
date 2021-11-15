package com.example.aturaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }

    fun resetOnClick(view: android.view.View) {
        val intent = Intent(this, ForgotPasswordConfirmActivity::class.java)
        startActivity(intent)
    }

    fun backOnClick(view: android.view.View) {
        finish()
    }
}