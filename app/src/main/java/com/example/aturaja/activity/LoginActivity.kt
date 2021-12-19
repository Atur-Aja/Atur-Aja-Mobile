package com.example.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.aturaja.MainActivity
import com.example.aturaja.R
import com.example.aturaja.model.LoginResponse
import com.example.aturaja.network.APIClient
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
        val email = textUsername.editText?.text.toString()
        val password = textPassword.editText?.text.toString()
        val apiCLient = APIClient()

        apiCLient.getApiService(this).loginUser(email, password)
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.code().equals(200)) {
                        response.body()?.let { SessionManager(applicationContext).saveAuthToken(it.accessToken) }
                        response.body()?.let { SessionManager(applicationContext).saveUsername(it.username) }
                        val intent = Intent(applicationContext, HomeActivity::class.java)

                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "email atau password salah", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("error lgoin", "$t")
                }

            })
    }

    override fun onStart() {
        super.onStart()

        if(SessionManager(this).isLoggedIn()) {
            val myIntent  = Intent(applicationContext, HomeActivity::class.java)

            startActivity(myIntent)
        }
    }
}
