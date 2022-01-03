package com.aturaja.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.ForgotPasswordResponse
import com.aturaja.aturaja.network.APIClient
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity() {
    private lateinit var etEmail: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.emailForgotPassword)
    }

    fun resetOnClick(view: android.view.View) {
        val email = etEmail.editText?.text?.trim().toString()

        callAPI(email)
    }

    fun backOnClick(view: android.view.View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun callAPI(email: String) {
        val apiClient = APIClient()
        val emailFormat = RequestBody.create("text/plain".toMediaTypeOrNull(), email)

        apiClient.getApiService(this).sendForgetPassword(emailFormat)
            .enqueue(object: Callback<ForgotPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    checkSuccessResponse(response)
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    failureResponse(t)
                }

            })
    }

    private fun checkSuccessResponse(response: Response<ForgotPasswordResponse>) {
        val message = "Reset password link sent on your email id."
        val failureMessage = "The selected email is invalid."

        if(response.body()?.message == message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, failureMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun failureResponse(t: Throwable) {
        Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
    }
}