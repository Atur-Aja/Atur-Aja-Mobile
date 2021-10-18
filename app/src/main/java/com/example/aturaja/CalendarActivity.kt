package com.example.aturaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalendarActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Schedule>
    lateinit var schedule : Array<String>
    lateinit var startHour : Array<String>
    lateinit var endHour : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        schedule = arrayOf(
            "tidur",
            "tidur",
            "tidur",
            "tidur",
            "tidur",
            "tidur",
        )

        startHour = arrayOf(
            "9:00 PM",
            "9:00 PM",
            "9:00 PM",
            "9:00 PM",
            "9:00 PM",
            "9:00 PM",
        )

        endHour = arrayOf(
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
        )

        newRecyclerView = findViewById(R.id.scheduleRecyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Schedule>()
        getUserData()

    }

    private fun getUserData() {

        for(i in schedule.indices){

            val schedule = Schedule(startHour[i],endHour[i],schedule[i])
            newArrayList.add(schedule)

        }

        newRecyclerView.adapter = ScheduleAdapter(newArrayList)

    }

    fun backOnClick(view: android.view.View) {
        finish()
    }

    fun addTaskOnClick(view: android.view.View) {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }

}