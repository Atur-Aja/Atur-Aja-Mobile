package com.example.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.ScheduleAdapter
import com.example.aturaja.model.GetScheduleResponse
import com.example.aturaja.model.Schedule
import com.example.aturaja.network.APIClient
import com.example.aturaja.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private var newArrayList = ArrayList<GetScheduleResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        newRecyclerView = findViewById(R.id.scheduleRecyclerView)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.layoutManager = LinearLayoutManager(this)

        getUserData()
    }

    private fun getUserData() {
        var apiClient = APIClient()
        var username = SessionManager(this).fetchUsername()

        if (username != null) {
            apiClient.getApiService(this).getSchedules(username)
                .enqueue(object: Callback<ArrayList<GetScheduleResponse>> {
                    override fun onResponse(
                        call: Call<ArrayList<GetScheduleResponse>>,
                        response: Response<ArrayList<GetScheduleResponse>>
                    ) {
                        response.body()?.let {newArrayList.addAll(it) }
                        newRecyclerView.adapter = ScheduleAdapter(newArrayList)
                    }

                    override fun onFailure(
                        call: Call<ArrayList<GetScheduleResponse>>,
                        t: Throwable
                    ) {
                        Log.d("error get schedule", "$t")
                    }
                })
        }
    }

    fun backOnClick(view: android.view.View) {
        startActivity(Intent (applicationContext, HomeActivity::class.java))
        finish()
    }

    fun addTaskOnClick(view: android.view.View) {
        startActivity(Intent (applicationContext, AddScheduleActivity::class.java))
        finish()
    }
}
