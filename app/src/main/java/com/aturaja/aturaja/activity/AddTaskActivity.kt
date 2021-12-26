package com.aturaja.aturaja.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.AutoCompleteFriendAdapter
import com.aturaja.aturaja.adapter.FriendsAdapterTask
import com.aturaja.aturaja.adapter.TodoAdapter
import com.aturaja.aturaja.model.AddTaskResponse
import com.aturaja.aturaja.model.GetFriendResponse
import com.aturaja.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTaskActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private lateinit var todoRecyclerView: RecyclerView
    private var todos = ArrayList<String>()
    private var dataFriends = ArrayList<GetFriendResponse>()

    private lateinit var memberRecyclerView: RecyclerView

    private lateinit var editTextTodo : EditText
    private lateinit var editTextDescription : EditText
    private lateinit var tvTime : TextView
    private lateinit var tvDate : TextView
    private lateinit var abTodo : ImageButton
    private lateinit var abFriends: ImageButton
    private lateinit var buttonSave : Button
    private lateinit var editTextTask : EditText
    private lateinit var buttonExit: ImageButton
    private lateinit var autoComplete: AutoCompleteTextView

    private lateinit var dateSave : String
    private lateinit var timeSave : String

    private lateinit var spinner: Spinner
    private var friends =  ArrayList<GetFriendResponse>()
    private var friendsDb = ArrayList<Int>()
    private var friendsString = ArrayList<String>()
    private var friendsChoose = ""

    private val dateFormatDb = SimpleDateFormat("yyyy-MM-dd")
    private val timeFormatDb = SimpleDateFormat("HH:mm:ss")
    private val TAG = "addTodo"

    private var bool = true
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
        setContentView(R.layout.activity_add_task_2)

        initComponent()

        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.setHasFixedSize(true)

        setSpinner()
        setTodayDate()
        getFriends()

        abTodo.setOnClickListener{
            addTodo()
        }

        buttonSave.setOnClickListener{
            saveTask()
        }

        buttonExit.setOnClickListener {
            startActivity(Intent(this, ListTaskActivity::class.java))
        }

        abFriends.setOnClickListener {
            friendsChoose = autoComplete.text.toString()
            autoComplete.setText("")

            checkFriends()
            showRecyclistFriends()
        }
    }

    private fun initComponent() {
        tvTime = findViewById(R.id.tvTime)
        tvDate = findViewById(R.id.tvDate)
        editTextTodo = findViewById(R.id.editTextAddTodo)
        abTodo = findViewById(R.id.abTodo)
        abFriends = findViewById(R.id.abMembers)
        buttonSave = findViewById(R.id.buttonSave)
        editTextTask = findViewById((R.id.editTextTask))
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonExit = findViewById(R.id.imageButton6)
        spinner = findViewById(R.id.spinner_priority_add_task)
        todoRecyclerView = findViewById(R.id.toDoRecyclerView)
        autoComplete = findViewById(R.id.auto_complete_friends_add_task)
        memberRecyclerView = findViewById(R.id.membersRecyclerView_add_task)
    }

    private fun setTodayDate() {
        val cal = Calendar.getInstance()
        val timeFormatView = SimpleDateFormat("h:mm a", Locale.US)

        dateSave = dateFormatDb.format(cal.time)
        timeSave = timeFormatDb.format(cal.time)

        tvTime.text = timeFormatView.format(cal.time)
    }
    private fun addTodo() {
        if(editTextTodo.text.isEmpty()) {
            Toast.makeText(this, "Masukkan Todo", Toast.LENGTH_SHORT).show()
        } else {
            todos.add(editTextTodo.text.toString())
            showRecyclistTodo()
        }
    }

    private fun showRecyclistTodo() {
        val adapter = TodoAdapter(todos)

        todoRecyclerView.adapter = adapter
        adapter.setOnClickDeleteTodo(object: TodoAdapter.OnClickDeleteTodo {
            override fun onClickItem(data: String) {
                todos.remove(data)
                showRecyclistTodo()
            }
        })
    }

    private fun showRecyclistFriends() {
        memberRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        memberRecyclerView.setHasFixedSize(true)

        val adapter =FriendsAdapterTask(friends)
        memberRecyclerView.adapter = adapter

        adapter.setOnButtonClickCallback(object: FriendsAdapterTask.OnButtonCLickCallback {
            override fun onClickButton(data: GetFriendResponse) {
                Log.d(TAG, "id = ${data.id}")
                friendsDb.remove(data.id.toInt())
                friends.remove(data)
                friendsString.remove(data.username)
                Log.d(TAG, "friends : ${friendsDb} \n ${friends}")
                showRecyclistFriends()
            }
        })
    }

    private fun checkFriends() {
        if(friendsString.contains(friendsChoose)) {
            Toast.makeText(this, "user sudah ditambahkan", Toast.LENGTH_SHORT).show()
        } else {
            friendsString.add(friendsChoose)
            for(i in dataFriends) {
                if (i.username == friendsChoose) {
                    friends.add(i)
                    friendsDb.add(i.id.toInt())
                }
            }
        }
    }

    private fun saveTask() {
        val apiClient = APIClient()
        val array = resources.getStringArray(R.array.priority_number)
        val title = editTextTask.text.toString()
        var myIntent = Intent(this, ListTaskActivity::class.java)
        val description = editTextDescription.text.toString()
        val priority = array[spinner.selectedItemPosition].toInt()

        if(title.isEmpty()) {
            Toast.makeText(this, "Masukkan judul", Toast.LENGTH_SHORT).show()
        } else {
            apiClient.getApiService(this).createTask(title, description, dateSave,timeSave,
                priority,
                todos,
                friendsDb)
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

    private fun getFriends() {
        val apiClient = APIClient()

        apiClient.getApiService(this).getFriends()
            .enqueue(object : Callback<List<GetFriendResponse>> {
                override fun onResponse(
                    call: Call<List<GetFriendResponse>>,
                    response: Response<List<GetFriendResponse>>
                ) {
                    if (response.code() == 200) {
                        response.body()?.let { dataFriends.addAll(it) }
                        response.body()?.let { setAutoComplete(it) }
                    }
                }

                override fun onFailure(call: Call<List<GetFriendResponse>>, t: Throwable) {
                    Log.d(TAG, "error get Friends : $t")
                }

            })
    }

    private fun setAutoComplete(body: List<GetFriendResponse>) {
        val adapter = AutoCompleteFriendAdapter(
            this, R.layout.friends_layout,
            body as MutableList<GetFriendResponse>
        )

        autoComplete.setAdapter(adapter)

        adapter.setOnFriendsClickCallback(object :
            AutoCompleteFriendAdapter.OnFriendsClickCallback {
            override fun onClickFriends(data: GetFriendResponse) {
                autoComplete.setText(data.username, false)
            }
        })
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