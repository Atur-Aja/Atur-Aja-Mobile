package com.example.aturaja

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class EditTaskActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoArrayList: ArrayList<Todo>
    private lateinit var todoName : Array<String>

    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var memberArrayList: ArrayList<Member>
    private lateinit var memberName : Array<Int>

    private lateinit var tvTime : TextView
    private lateinit var tvDate : TextView

    private var hour = 0
    private var minute = 0
    private var date = 0
    private var month = 0
    private var year = 0

    private var savedHour = 0
    private var savedMinute = 0
    private var savedDate = 0
    private var savedMonth = 0
    private var savedYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        tvTime = findViewById(R.id.tvTime)
        tvDate = findViewById(R.id.tvDate)

        todoName = arrayOf(
            "tidur",
            "Makan",
        )

        todoRecyclerView = findViewById(R.id.toDoRecyclerView)
        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.setHasFixedSize(true)

        todoArrayList = arrayListOf()
        getUserData()

        memberName = arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
        )

        memberRecyclerView = findViewById(R.id.membersRecyclerView)
        memberRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        memberRecyclerView.setHasFixedSize(true)

        memberArrayList = arrayListOf()
        getMemberData()

    }

    private fun getTime() {
        val cal : Calendar = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        date = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }



    private fun getUserData() {

        for(i in todoName.indices){

            val todo = Todo(todoName[i])
            todoArrayList.add(todo)

        }

        todoRecyclerView.adapter = TodoAdapter(todoArrayList)

    }

    private fun getMemberData() {

        for(i in memberName.indices){

            val member = Member(memberName[i])
            memberArrayList.add(member)

        }

        memberRecyclerView.adapter = MemberAdapter(memberArrayList)

    }

    fun saveOnClick(view: android.view.View) {

    }

    fun editTimeOnClick(view: android.view.View) {
        getTime()
        TimePickerDialog(this,this,hour,minute,false).show()
    }

    fun editDateOnClick(view: android.view.View) {
        getTime()
        DatePickerDialog(this,this,year,month,date).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour = p1
        savedMinute = p2

        tvTime.text = "$savedHour : $savedMinute"
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedYear = p1
        savedMonth = p2
        savedDate = p3

        tvDate.text = "$savedDate-$savedMonth-$savedYear"

    }


}