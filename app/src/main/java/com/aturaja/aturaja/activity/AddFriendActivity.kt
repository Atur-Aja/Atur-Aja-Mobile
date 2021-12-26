package com.aturaja.aturaja.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.AddFriendAdapter
import com.aturaja.aturaja.model.AddFriendResponse
import com.aturaja.aturaja.model.GetFriendResponse
import com.aturaja.aturaja.model.GetSearchResponseItem
import com.aturaja.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response








class AddFriendActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<GetSearchResponseItem>
    private lateinit var friendName : Array<String>
    private lateinit var friendEmail : Array<String>
    private lateinit var friendImage : Array<Int>
    private lateinit var etSearchUsername : EditText
    private var dataFriens = ArrayList<GetFriendResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        etSearchUsername = findViewById(R.id.etSearchUsername)

        etSearchUsername.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                getSearchdata()
            }
            false
        })
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
                    if(response.code().equals(200)){
                        setRecyclerView(response.body() as ArrayList<GetSearchResponseItem>)
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

    private fun getFriends() {
        val apiCLient = APIClient()

        apiCLient.getApiService(this).getFriends()
            .enqueue(object: Callback<List<GetFriendResponse>> {
                override fun onResponse(
                    call: Call<List<GetFriendResponse>>,
                    response: Response<List<GetFriendResponse>>
                ) {
                    if(response.code() == 200) {
                        response.body()?.let { dataFriens.addAll(it) }
                    }
                }

                override fun onFailure(call: Call<List<GetFriendResponse>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun setRecyclerView(arrayList: ArrayList<GetSearchResponseItem>) {
        val addfriendAdapter = AddFriendAdapter(arrayList)
        val apiClient = APIClient()

        recyclerView = findViewById(R.id.addFriendRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = addfriendAdapter

        addfriendAdapter.setOnAddClickCallback(object : AddFriendAdapter.OnAddClickCallBack{
            override fun onAddClicked(data: GetSearchResponseItem) {
                apiClient.getApiService(this@AddFriendActivity).addFriend(data.id.toString())
                    .enqueue(object : Callback<AddFriendResponse>{
                        override fun onResponse(
                            call: Call<AddFriendResponse>,
                            response: Response<AddFriendResponse>
                        ) {
                            Log.d("Success adding friend", "${response.body()}")
                            if(response.code().equals(200)){
                                getSearchdata()
                            }
                        }

                        override fun onFailure(call: Call<AddFriendResponse>, t: Throwable) {
                            Log.d("Error adding friend", "$t")
                        }

                    })
            }

        })
    }
}