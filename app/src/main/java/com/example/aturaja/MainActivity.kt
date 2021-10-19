package com.example.aturaja

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aturaja.activity.LoginActivity
import com.example.aturaja.model.RegisterResponse
import com.example.aturaja.network.RetrofitClient
import com.example.aturaja.session.SessionManager
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var buttonRegister: Button
    private lateinit var textUsername: TextInputLayout
    private lateinit var textEmail: TextInputLayout
    private lateinit var textPassword: TextInputLayout
    private lateinit var textConfirmPassword: TextInputLayout
    private lateinit var textPhoneNumber: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponent()

        buttonRegister.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val username = textUsername.editText?.text.toString()
                val email = textEmail.editText?.text.toString()
                val password = textPassword.editText?.text.toString()
                val confirmPassword = textConfirmPassword.editText?.text.toString()
                val phoneNumber = textPhoneNumber.editText?.text.toString()

                RetrofitClient.instance.createUser(username, email, password, confirmPassword, phoneNumber)
                    .enqueue(object: Callback<RegisterResponse> {
                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            if(response.code().equals(201)) {
                                Toast.makeText(applicationContext, "Register sukses", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(applicationContext, "username sudah terpakai", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            }
        })
    }

    fun initComponent () {
        buttonRegister= findViewById(R.id.signup)
        textUsername = findViewById(R.id.outlinedUsername)
        textEmail = findViewById(R.id.outlinedemail)
        textPassword = findViewById(R.id.outlinedpassword)
        textConfirmPassword = findViewById(R.id.outlinedconfirmpassword)
        textPhoneNumber = findViewById(R.id.phonenumber)
    }

    fun signInCLick(view: View) {
        val myIntent = Intent(this@MainActivity, LoginActivity::class.java)

        startActivity(myIntent)
    }
}