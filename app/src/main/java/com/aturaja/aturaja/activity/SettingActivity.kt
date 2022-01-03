package com.aturaja.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.GetProfileResponse
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : AppCompatActivity() {
    private lateinit var tvEmail: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        tvEmail = findViewById(R.id.tvEmail)

        getProfilUser()
    }

    fun passwordSettingOnClick(view: View) {
        startActivity(Intent(this, PasswordSettingActivity::class.java))
    }


    fun emailSettingOnClick(view: View) {
        val intent = Intent(this, EmailSettingActivity::class.java)
        intent.putExtra("email", tvEmail.text.toString())

        startActivity(intent)
    }

    fun backOnClick(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun getProfilUser() {
        val apiClient = APIClient()
        val username = SessionManager(this).fetchUsername()

        username?.let {
            apiClient.getApiService(this).getProfile(it)
                .enqueue(object: Callback<GetProfileResponse> {
                    override fun onResponse(
                        call: Call<GetProfileResponse>,
                        response: Response<GetProfileResponse>
                    ) {
                        if(response.code() == 200) {
                            tvEmail.text = response.body()?.email
                        }
                    }

                    override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                        failureResponse(t)
                    }

                })
        }
    }

    private fun failureResponse(t: Throwable) {
        Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
    }
}