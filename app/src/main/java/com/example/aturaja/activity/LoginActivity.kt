package com.example.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.aturaja.MainActivity
import com.example.aturaja.R
import com.example.aturaja.model.LoginResponse
import com.example.aturaja.network.RetrofitClient
import com.example.aturaja.session.SessionManager
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var textUsername: TextInputLayout
    private lateinit var textPassword: TextInputLayout
    private lateinit var textSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textUsername = findViewById(R.id.editTextTextPersonName)
        textPassword = findViewById(R.id.editTextPassword)
        textSignUp = findViewById(R.id.signUpTV)

        textSignUp.setOnClickListener {
            val myIntent = Intent(applicationContext, MainActivity::class.java)

            startActivity(myIntent)
        }
    }

    fun forgotPasswordOnClick(view: android.view.View) {
        startActivity(Intent(applicationContext, ForgotPassword::class.java))
    }

    fun signInOnClick(view: android.view.View) {
        var email = textUsername.editText?.text.toString()
        var password = textPassword.editText?.text.toString()

            RetrofitClient.instance.loginUser(email, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.code().equals(200)) {
                            SessionManager.getInstance(applicationContext).saveToken(response.body()?.accessToken)
                            val intent = Intent(applicationContext, HomeActivity::class.java)

                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "email atau password salah", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }

    override fun onStart() {
        super.onStart()

        if(SessionManager.getInstance(this).isLoggedIn()) {
            val myIntent  = Intent(applicationContext, HomeActivity::class.java)

            startActivity(myIntent)
        }
    }
}
