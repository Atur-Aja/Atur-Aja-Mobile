package com.example.aturaja.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.TodoAdapter
import com.example.aturaja.model.AddTaskResponse
import com.example.aturaja.model.Todo
import com.example.aturaja.network.APIClient
import com.example.aturaja.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTaskActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoArrayList: ArrayList<Todo>
    private var todos = ArrayList<String>()

    private lateinit var memberRecyclerView: RecyclerView

    private lateinit var editTextTodo : EditText
    private lateinit var editTextDescription : EditText
    private lateinit var tvTime : TextView
    private lateinit var tvDate : TextView
    private lateinit var abTodo : ImageButton
    private lateinit var buttonSave : Button
    private lateinit var editTextTask : EditText
    private lateinit var buttonExit: ImageButton

    private lateinit var dateSave : String
    private lateinit var timeSave : String

    private lateinit var spinner: Spinner
    private var friends =  ArrayList<Int>()

    private val dateFormatDb = SimpleDateFormat("yyyy-MM-dd")
    private val timeFormatDb = SimpleDateFormat("HH:mm:ss")
    private val TAG = "addTodo"

    var hour = 0
    var minute = 0
    var date = 0
    var month = 0
    var year = 0

    var savedHour = 0
    var savedMinute = 0
    var savedDate = 0
    var savedMonth = 0
    var savedYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initComponent()

        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.setHasFixedSize(true)

        todoArrayList = arrayListOf()

        setSpinner()
        setTodayDate()
        abTodo.setOnClickListener{
            var todo = editTextTodo.text.toString()

            todos.add(todo)
            showRecyclist()
        }

        buttonSave.setOnClickListener{
            saveTask()
        }

        buttonExit.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }

        memberRecyclerView = findViewById(R.id.membersRecyclerView)
        memberRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        memberRecyclerView.setHasFixedSize(true)

    }

    private fun initComponent() {
        tvTime = findViewById(R.id.tvTime)
        tvDate = findViewById(R.id.tvDate)
        editTextTodo = findViewById(R.id.editTextAddTodo)
        abTodo = findViewById(R.id.abTodo)
        buttonSave = findViewById(R.id.buttonSave)
        editTextTask = findViewById((R.id.editTextTask))
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonExit = findViewById(R.id.imageButton6)
        spinner = findViewById(R.id.spinner_priority_add_task)
        todoRecyclerView = findViewById(R.id.toDoRecyclerView)
    }

    private fun setTodayDate() {
        val cal = Calendar.getInstance()
        val timeFormatView = SimpleDateFormat("h:mm a", Locale.US)

        dateSave = dateFormatDb.format(cal.time)
        timeSave = timeFormatDb.format(cal.time)

        tvTime.text = timeFormatView.format(cal.time)
    }

    private fun showRecyclist() {
        val adapter = TodoAdapter(todos)

        todoRecyclerView.adapter = adapter

        adapter.setOnClickDeleteTodo(object: TodoAdapter.OnClickDeleteTodo {
            override fun onClickItem(data: String) {
                todos.remove(data)
                showRecyclist()
            }

        })
    }

    private fun saveTask() {
        val apiClient = APIClient()
        val array = resources.getStringArray(R.array.priority_number)
        val title = editTextTask.text.toString()
        var myIntent = Intent(this, TaskActivity::class.java)
        val description = editTextDescription.text.toString()
        val priority = array[spinner.selectedItemPosition].toInt()

        friends.add(2)

        if(title.isEmpty()) {
            Toast.makeText(this, "Masukkan judul", Toast.LENGTH_SHORT).show()
        } else {
            apiClient.getApiService(this).createTask(title, description, dateSave,timeSave,
                priority,
                todos,
                friends)
                .enqueue(object : Callback<AddTaskResponse>{
                    override fun onResponse(
                        call: Call<AddTaskResponse>,
                        response: Response<AddTaskResponse>
                    ) {
                        if(response.code().equals(201)) {
                            Toast.makeText(applicationContext, "Task created", Toast.LENGTH_SHORT).show()
                            startActivity(myIntent)
                        }

                        Log.d(TAG, "$response")
                    }

                    override fun onFailure(call: Call<AddTaskResponse>, t: Throwable) {
                        Log.d("Create Task", "$t")
                    }

                })
        }
    }

    private fun setSpinner() {
        val priority = resources.getStringArray(R.array.priority)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, priority
        )

        spinner.adapter = adapter
    }

    private fun getTime() {
        val cal : Calendar = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        date = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

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
        timeSave = "$savedHour:$savedMinute"
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedYear = p1
        savedMonth = p2
        savedDate = p3

        tvDate.text = "$savedYear-$savedMonth-$savedDate"
        dateSave = "$savedYear-$savedMonth-$savedDate"

    }
}