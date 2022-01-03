package com.aturaja.aturaja.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.aturaja.aturaja.R
import com.aturaja.aturaja.dialog.ConfirmPasswordDialog
import com.aturaja.aturaja.model.ChangePasswordResponse
import com.aturaja.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordSettingActivity : AppCompatActivity(), ConfirmPasswordDialog.DialogPasswordListener{
    private lateinit var etPassword: EditText
    private lateinit var etConfPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_setting)

        initComponent()
    }

    private fun initComponent() {
        etPassword = findViewById(R.id.etNewPassword)
        etConfPassword = findViewById(R.id.etNewPasswordValidate)
    }

    fun backOnClick(view: View) {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    override fun onFinishDialog(choose: Boolean) {
        if(choose) {
            saveNewPassword()
        }
    }

    fun confirmPassword(view: View) {
        val dialog = ConfirmPasswordDialog(this)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun saveNewPassword() {
        val password = etPassword.text.trim().toString()
        val passwordVal = etConfPassword.text.trim().toString()

        if(password.isNotEmpty() && passwordVal.isNotEmpty()) {
            callAPI(password, passwordVal)
        } else {
            Toast.makeText(this, "input password and password validate", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callAPI(password: String, passwordVal: String) {
        val apiClient = APIClient()

        apiClient.getApiService(this).changePassword(password, passwordVal)
            .enqueue(object: Callback<ChangePasswordResponse> {
                override fun onResponse(
                    call: Call<ChangePasswordResponse>,
                    response: Response<ChangePasswordResponse>
                ) {
                    checkSuccessResponse(response)
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    failureResponse(t)
                }

            })
    }

    private fun checkSuccessResponse(response: Response<ChangePasswordResponse>) {
        if(response.code() == 201) {
            Toast.makeText(this, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "the given data was invalid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun failureResponse(t: Throwable) {
        Toast.makeText(this@PasswordSettingActivity, "$t", Toast.LENGTH_SHORT).show()
    }
}