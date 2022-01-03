package com.aturaja.aturaja.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.AddFriendAdapter
import com.aturaja.aturaja.model.AddFriendResponse
import com.aturaja.aturaja.model.FriendsRecyclerAddFriend
import com.aturaja.aturaja.model.GetSearchResponseItem
import com.aturaja.aturaja.network.APIClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response








class AddFriendActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearchUsername : EditText

    private var arrayList = ArrayList<GetSearchResponseItem>()
    private var bitmapArray = ArrayList<Bitmap>()
    private var arrayRecycler = ArrayList<FriendsRecyclerAddFriend>()

    private val TAG = "addFriends"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        etSearchUsername = findViewById(R.id.etSearchUsername)
    }

    private fun getSearchdata() {
        val apiClient = APIClient()

        apiClient.getApiService(this).getSearch(etSearchUsername.text.toString())
            .enqueue(object : Callback<List<GetSearchResponseItem>> {
                override fun onResponse(
                    call: Call<List<GetSearchResponseItem>>,
                    response: Response<List<GetSearchResponseItem>>
                ) {
                    Log.d("Success getting friends", "${response.body()?.size}")
                    if(response.code() == 200) {
                        response.body()?.let {
                            arrayList.clear()
                            arrayList.addAll(it)
                            getDataRecycler()
                        }
                    }
                }

                override fun onFailure(call: Call<List<GetSearchResponseItem>>, t: Throwable) {
                    Log.d("Error getting friends", "$t")
                }
            })
    }

    fun backOnClick(view: android.view.View) {
        finish()
    }

    fun searchOnClick(view: android.view.View) {
        getSearchdata()
    }

    private fun getDataRecycler() {
        for (i in arrayList) {
            getImageUser(i)
            Log.d(TAG, "data array : $i")
        }
    }

    private fun getImageUser(data: GetSearchResponseItem) {
        val apiClient = APIClient()
        var bitmap: Bitmap

        apiClient.getApiService(this).getPhoto(data.photo.toString())
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.code() == 200) {
                        bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                        val model = FriendsRecyclerAddFriend(data, bitmap)
                        arrayRecycler.add(model)
                        if(arrayRecycler.size == arrayList.size) {
                            setRecyclerView()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "Error : $t")
                }
            })
    }

    private fun setRecyclerView() {
        val addFriendAdapter = AddFriendAdapter(arrayRecycler)
        val apiClient = APIClient()

        recyclerView = findViewById(R.id.addFriendRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = addFriendAdapter

        addFriendAdapter.setOnAddClickCallback(object : AddFriendAdapter.OnAddClickCallBack{
            override fun onAddClicked(data: FriendsRecyclerAddFriend) {
                apiClient.getApiService(this@AddFriendActivity).addFriend(data.data.id.toString())
                    .enqueue(object : Callback<AddFriendResponse>{
                        override fun onResponse(
                            call: Call<AddFriendResponse>,
                            response: Response<AddFriendResponse>
                        ) {
                            checkSuccessResponse(response)
                        }

                        override fun onFailure(call: Call<AddFriendResponse>, t: Throwable) {
                            Toast.makeText(this@AddFriendActivity, t.toString(), Toast.LENGTH_SHORT).show()
                        }

                    })
            }

        })
    }

    private fun checkSuccessResponse(response: Response<AddFriendResponse>) {
        if(response.code() == 200) {
            Toast.makeText(this, response.body()?.message, Toast.LENGTH_SHORT).show()
        } else if(response.code() == 409) {
            Toast.makeText(this, "you have invited him or her", Toast.LENGTH_SHORT).show()
        }
    }

}