package com.example.aturaja


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.model.AddTaskResponse
import com.example.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList

class AddTaskActivity : AppCompatActivity(), OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoArrayList: ArrayList<Todo>
    private var todoName = mutableListOf<String>()

    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var memberArrayList: ArrayList<Member>
    private lateinit var memberName : Array<Int>

    private lateinit var editTextTodo : EditText
    private lateinit var editTextDescription : EditText
    private lateinit var tvTime : TextView
    private lateinit var tvDate : TextView
    private lateinit var abTodo : ImageButton
    private lateinit var buttonSave : Button
    private lateinit var editTextTask : EditText

    private lateinit var dateSave : String
    private lateinit var timeSave : String

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

        tvTime = findViewById(R.id.tvTime)
        tvDate = findViewById(R.id.tvDate)
        editTextTodo = findViewById(R.id.editTextAddTodo)
        abTodo = findViewById(R.id.abTodo)
        buttonSave = findViewById(R.id.buttonSave)
        editTextTask = findViewById((R.id.editTextTask))
        editTextDescription = findViewById(R.id.editTextDescription)

        todoRecyclerView = findViewById(R.id.toDoRecyclerView)
        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.setHasFixedSize(true)

        todoArrayList = arrayListOf()



        abTodo.setOnClickListener{

            var todo = editTextTodo.text.toString()

            todoArrayList.add(Todo(todo))
            todoName.add(todo)

            todoRecyclerView.adapter = TodoAdapter(todoArrayList)
        }

        buttonSave.setOnClickListener{
            val apiClient = APIClient()
            val title = editTextTask.text.toString()
            val desc = editTextDescription.text.toString()

            Log.d("List Todo","$todoName")
            apiClient.getApiService(this).addTask(title,desc,dateSave,timeSave,todoName)
                .enqueue(object : retrofit2.Callback<AddTaskResponse>{
                    override fun onResponse(
                        call: Call<AddTaskResponse>,
                        response: Response<AddTaskResponse>
                    ) {
                        Log.d("Create Task Success", "${response.code()}")
                    }

                    override fun onFailure(call: Call<AddTaskResponse>, t: Throwable) {
                        Log.d("Create Task", "$t")
                    }

                })
        }

        memberName = arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
        )

        memberRecyclerView = findViewById(R.id.membersRecyclerView)
        memberRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
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



    private fun getMemberData() {

        for(i in memberName.indices){

            val member = Member(memberName[i])
            memberArrayList.add(member)

        }

        memberRecyclerView.adapter = MemberAdapter(memberArrayList)

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