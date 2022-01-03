package com.aturaja.aturaja.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.DeleteFriendRespose
import com.aturaja.aturaja.model.FriendsRecyclerWaitingList
import com.aturaja.aturaja.model.GetAllFriendRequestResponse
import com.aturaja.aturaja.model.GetProfileResponse
import com.aturaja.aturaja.network.APIClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilFriendsActivity : AppCompatActivity() {
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var imageView: ImageView

    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_friends)

        initComponent()
        getData()
    }

    private fun initComponent() {
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.textView8)
        imageView = findViewById(R.id.imageView4)
    }

    private fun getData() {
        val data = intent


        if(data.getStringExtra("username") != null) {
            callApi(data.getStringExtra("username")!!)
        }
    }

    private fun callApi(stringExtra: String) {
        val apiClient = APIClient()

        apiClient.getApiService(this).getProfile(stringExtra)
            .enqueue(object : Callback<GetProfileResponse> {
                override fun onResponse(
                    call: Call<GetProfileResponse>,
                    response: Response<GetProfileResponse>
                ) {
                    checkSuccessResponse(response)
                }

                override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun checkSuccessResponse(response: Response<GetProfileResponse>) {
        if(response.code() == 200) {
            tvName.text = response.body()?.username
            tvPhone.text = response.body()?.phoneNumber
            tvEmail.text = response.body()?.email
            userId = response.body()?.id.toString()
            getImageUser(response.body()?.photo.toString())
        }
    }

    private fun getImageUser(imageName: String) {
        val apiClient = APIClient()
        var bitmap: Bitmap


        apiClient.getApiService(this).getPhoto(imageName)
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.code() == 200) {
                        bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                        imageView.setImageBitmap(bitmap)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@ProfilFriendsActivity, "$t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun backOnClick(view: View) {
        startActivity(Intent(this, FriendListActivity::class.java))
    }

    fun deleteOnClick(view: View) {
        val apiClient = APIClient()
        val jsonParams: MutableMap<String, Any> = ArrayMap()

        jsonParams["user_id"] = userId
        val id = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            (JSONObject(jsonParams as Map<*, *>)).toString())

        apiClient.getApiService(this).deleteFriend(id)
            .enqueue(object: Callback<DeleteFriendRespose> {
                override fun onResponse(
                    call: Call<DeleteFriendRespose>,
                    response: Response<DeleteFriendRespose>
                ) {
                    if(response.code() == 200) {
                        Toast.makeText(this@ProfilFriendsActivity, "friend deleted successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ProfilFriendsActivity, FriendListActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<DeleteFriendRespose>, t: Throwable) {
                    Toast.makeText(this@ProfilFriendsActivity, "$t", Toast.LENGTH_SHORT).show()
                }

            })

    }
}