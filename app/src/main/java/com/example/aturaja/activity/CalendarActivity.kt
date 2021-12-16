package com.example.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.ScheduleAdapter
import com.example.aturaja.model.GetAllScheduleResponse
import com.example.aturaja.model.GetScheduleResponse
import com.example.aturaja.model.ScheduleElement
import com.example.aturaja.model.SchedulesItem
import com.example.aturaja.network.APIClient
import com.example.aturaja.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CalendarActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private var newArrayList = ArrayList<SchedulesItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        newRecyclerView = findViewById(R.id.scheduleRecyclerView)


        getUserData()
    }

    private fun getUserData() {
        val username = SessionManager(this).fetchUsername()
        val  schedule = ArrayList<GetScheduleResponse>()
        var apiClient = APIClient()

        apiClient.getApiService(this).getSchedules()
            .enqueue(object: Callback<GetAllScheduleResponse> {
                override fun onResponse(
                    call: Call<GetAllScheduleResponse>,
                    response: Response<GetAllScheduleResponse>
                ) {
                    response.body()?.let { showRecyclist(it.schedules) }
                    response.body()?.let {
                        if(it.schedules != null) {
                            newArrayList.addAll(it.schedules)
                        }
                    }
                }

                override fun onFailure(call: Call<GetAllScheduleResponse>, t: Throwable) {
                    Log.d("calendar", "$t")
                }
//                override fun onResponse(
//                    call: Call<GetScheduleResponse>,
//                    response: Response<GetScheduleResponse>
//                ) {
//                    response.body()?.let { newArrayList.addAll(it) }
//                    Log.i("calendar", "$newArrayList")
//                    newRecyclerView.adapter = ScheduleAdapter(newArrayList)
//                }
//                override fun onFailure(call: Call<ArrayList<GetScheduleResponse>>, t: Throwable) {
//                    Log.i("calendar", "gagal")
//                    Log.i("calendar", "$t")
//                }
            })

//        return schedule
//
//        if (username != null) {
//            Log.d("schedule return", "${DataSchedule().loadScheduleDB(this, username)}")
//            newArrayList = DataSchedule().loadScheduleDB(this, username)
//            Log.d("schedule", "${newArrayList}")
//            newRecyclerView.adapter = ScheduleAdapter(newArrayList)
//        }
    }

    fun showRecyclist(schedules: List<SchedulesItem>) {
        val scheduleListAdapter = ScheduleAdapter(schedules as ArrayList<SchedulesItem>)
        val intent = Intent(this, EditDeleteSchedule::class.java)

        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.adapter = scheduleListAdapter

        scheduleListAdapter.setOnItemClickCallback(object : ScheduleAdapter.OnItemClickCallback {
            override fun onClickItem(data: SchedulesItem) {
                intent.putExtra("schedule_data", data)
                startActivity(intent)
            }

        })
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
