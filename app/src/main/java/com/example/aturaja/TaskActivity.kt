package com.example.aturaja

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskActivity : AppCompatActivity() {

    private lateinit var tvTodayDate : TextView
    private lateinit var recyclerViewTask: RecyclerView
    private lateinit var arrayListTask: ArrayList<Task>
    private lateinit var taskName : Array<String>
    private lateinit var taskHour : Array<String>
    private lateinit var taskDate : Array<String>
    private lateinit var recyclerViewGroup: RecyclerView
    private lateinit var arrayListGroup: ArrayList<Group>
    private lateinit var groupName : Array<String>
    private lateinit var groupHour : Array<String>
    private lateinit var groupDate : Array<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        tvTodayDate = findViewById(R.id.tvTodayDate)
        showDate(tvTodayDate)

        taskName = arrayOf(
            "tidur",
            "tidur",
            "tidur",
            "tidur",
            "tidur",
            "tidur",
        )


        taskHour = arrayOf(
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
        )

        taskDate = arrayOf(
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
        )

        recyclerViewTask = findViewById(R.id.taskRecyclerView)
        recyclerViewTask.layoutManager = LinearLayoutManager(this)
        recyclerViewTask.setHasFixedSize(true)

        arrayListTask = arrayListOf()
        getTaskData()

        groupName = arrayOf(
            "tidur bareng",
            "tidur bareng",
            "tidur bareng",
            "tidur bareng",
            "tidur bareng",
            "tidur bareng",
        )


        groupHour = arrayOf(
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
            "6:00 AM",
        )

        groupDate = arrayOf(
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
            "25 October 2021",
        )

        recyclerViewGroup = findViewById(R.id.groupRecyclerView)
        recyclerViewGroup.layoutManager = LinearLayoutManager(this)
        recyclerViewGroup.setHasFixedSize(true)

        arrayListGroup = arrayListOf()
        getGroupData()


    }

    private fun getTaskData() {

        for(i in taskName.indices){

            val task = Task(taskName[i],taskHour[i],taskDate[i])
            arrayListTask.add(task)

        }

        recyclerViewTask.adapter = TaskAdapter(arrayListTask)

    }

    private fun getGroupData() {

        for(i in groupName.indices){

            val group = Group(groupName[i],groupHour[i],groupDate[i])
            arrayListGroup.add(group)

        }

        recyclerViewGroup.adapter = GroupAdapter(arrayListGroup)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDate (view: TextView){
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val date : String
        date = formatter.format(currentDateTime)
        view.setText(date)
    }

    fun addTaskOnClick(view: android.view.View) {
        val intent = Intent(this, FriendListActivity::class.java)
        startActivity(intent)
    }
}


