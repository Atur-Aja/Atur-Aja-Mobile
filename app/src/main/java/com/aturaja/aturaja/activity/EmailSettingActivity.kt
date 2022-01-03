package com.aturaja.aturaja.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.aturaja.aturaja.R
import com.aturaja.aturaja.dialog.ConfirmEmailDialog
import com.aturaja.aturaja.model.ChangeEmailResponse
import com.aturaja.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailSettingActivity : AppCompatActivity(), ConfirmEmailDialog.DialogEmailListener {
    private val TAG = "EmailSetting"
    private lateinit var etEmail: EditText
    private lateinit var tvOldEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_setting)

        etEmail = findViewById(R.id.etNewEmail)
        tvOldEmail = findViewById(R.id.tvEmail)

        fetchData()
    }

    private fun fetchData() {
        val data = intent

        if(data!=null) {
            tvOldEmail.text = data.getStringExtra("email")
        }
    }

    fun backOnClick(view: android.view.View) {
        startActivity(Intent(this, SettingActivity::class.java))
        finish()
    }

    fun saveOnClick(view: android.view.View) {
        var dialog = ConfirmEmailDialog(this)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onFinishDialog(choose: Boolean) {
        if(choose) {
            setEmail()
        }
    }

    private fun setEmail() {
        val newEmail = etEmail.text.trim().toString()

        if(newEmail.isNotEmpty()) {
            callAPI(newEmail)
        }
    }

    private fun callAPI(newEmail: String) {
        val apiClient = APIClient()

        apiClient.getApiService(this).changeEmail(newEmail)
            .enqueue(object: Callback<ChangeEmailResponse> {
                override fun onResponse(
                    call: Call<ChangeEmailResponse>,
                    response: Response<ChangeEmailResponse>
                ) {
                    checkSuccesResponse(response)
                }

                override fun onFailure(call: Call<ChangeEmailResponse>, t: Throwable) {
                    failurResponse(t)
                }

            })
    }

    private fun checkSuccesResponse(response: Response<ChangeEmailResponse>) {
        if(response.code() == 201) {
            Toast.makeText(this, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "error : ${response.body()?.errors?.email}")
            Toast.makeText(this, "${response.body()?.errors}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun failurResponse(t: Throwable) {
        Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
    }


}