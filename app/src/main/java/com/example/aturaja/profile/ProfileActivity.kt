package com.example.aturaja.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aturaja.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    fun backOnClick(view: android.view.View) {
        finish()
    }
}