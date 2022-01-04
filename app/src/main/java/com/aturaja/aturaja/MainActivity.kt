package com.aturaja.aturaja

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aturaja.aturaja.activity.LoginActivity
import com.aturaja.aturaja.model.RegisterResponse
import com.aturaja.aturaja.network.APIClient
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var buttonRegister: Button
    private lateinit var textUsername: TextInputLayout
    private lateinit var textEmail: TextInputLayout
    private lateinit var textPassword: TextInputLayout
    private lateinit var textConfirmPassword: TextInputLayout
    private lateinit var progressBar: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        initComponent()
    }

    fun initComponent () {
        buttonRegister= findViewById(R.id.signup)
        textUsername = findViewById(R.id.outlinedUsername)
        textEmail = findViewById(R.id.outlinedemail)
        textPassword = findViewById(R.id.outlinedpassword)
        textConfirmPassword = findViewById(R.id.outlinedconfirmpassword)
        progressBar = findViewById(R.id.progerss_bar)
    }

    fun signUpClick(view: View) {
        val username = textUsername.editText?.text.toString()
        val email = textEmail.editText?.text.toString()
        val password = textPassword.editText?.text.toString()
        val confirmPassword = textConfirmPassword.editText?.text.toString()
        val apiClient = APIClient()

        if(username.trim().isNotEmpty() && email.trim().isNotEmpty() && password.trim().isNotEmpty() && confirmPassword.trim().isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            apiClient.getApiService(this).createUser(email, username, password, confirmPassword)
                .enqueue(object: Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if(response.code() == 201) {
                            progressBar.visibility = View.GONE
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            Toast.makeText(applicationContext, "registrasi success, please check your email", Toast.LENGTH_LONG).show()

                            startActivity(intent)
                        } else {
                            try {
                                progressBar.visibility = View.GONE
                                val json = response.errorBody()
                                val json2 = json.toString()
                                val jObjError = JSONObject(json2.substring(json2.indexOf("{"), json2.lastIndexOf("}") + 2))
                                Toast.makeText(applicationContext, "${jObjError}", Toast.LENGTH_LONG).show()
                            }catch (e: Exception) {
                                progressBar.visibility = View.GONE
                                Toast.makeText(applicationContext, "$e", Toast.LENGTH_LONG).show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Log.d("error register", "$t")
                    }

                })
        }
    }

    fun signInCLick(view: View) {
        val myIntent = Intent(this@MainActivity, LoginActivity::class.java)

        startActivity(myIntent)
    }
}