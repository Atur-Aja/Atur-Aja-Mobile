package com.aturaja.aturaja.activity

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.AutoCompleteFriendAdapter
import com.aturaja.aturaja.adapter.FriendsAdapterTask
import com.aturaja.aturaja.adapter.TodoAdapterEditDelete
import com.aturaja.aturaja.model.*
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.service.AlarmBroadcast
import com.aturaja.aturaja.session.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditDeleteTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var tvTime : TextView
    private lateinit var tvDate : TextView
    private lateinit var buttonSave : Button
    private lateinit var editTextTask : EditText
    private lateinit var dateSave : String
    private lateinit var timeSave : String
    private lateinit var btnExit: ImageButton
    private lateinit var btnDelete: Button
    private lateinit var spinner: Spinner
    private lateinit var recycler: RecyclerView
    private lateinit var editTextDescrpition: EditText
    private lateinit var btnAddTodo: ImageButton
    private lateinit var editTextTodo: EditText
    private lateinit var autoComplete: AutoCompleteTextView
    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var btnAddFriends: ImageButton

    private val timeFormatDB = SimpleDateFormat("HH:mm")
    private val timeFormatView = SimpleDateFormat("hh:mm a")
    private val dateFormatView = SimpleDateFormat("dd MMMM yyyy")
    private val dateFormatDB = SimpleDateFormat("yyyy-MM-dd")

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
    private var userId = 0


    private var todos = ArrayList<String>()
    private var todosItem = ArrayList<TodoItem>()
    private var todoItemDelete = ArrayList<TodoItem>()
    private var todoItemUpdate = ArrayList<TodoUpdate>()
    private var friends =  ArrayList<GetFriendResponse>()
    private var friendsDb = ArrayList<Int>()
    private var friendsChoose = ""
    private var arrayRecycler = ArrayList<ArrayFriendsTask>()
    private var dataFriends = ArrayList<GetFriendResponse>()
    private var friendsString = ArrayList<String>()
    private var bitmapArray = ArrayList<Bitmap>()

    private lateinit var priorityList: Array<String>
    private lateinit var priorityListNumber: Array<String>
    private lateinit var updatedAt: String
    private lateinit var oldTitle: String

    private var status: Int?= 1

    private var id: Int = 0
    private var TAG = "edittask"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_delete_task)

        InitComponent()
        getFriends()

        btnAddTodo.setOnClickListener {
            addTodo()
        }

        buttonSave.setOnClickListener {
            saveTask()
        }

        btnDelete.setOnClickListener {
            deleteTask()
        }

        btnExit.setOnClickListener {
            toTaskActivity()
        }

        btnAddFriends.setOnClickListener {
            friendsChoose = autoComplete.text.toString()
            autoComplete.setText("")

            checkFriends()
        }

        fetchData()
    }

    fun InitComponent() {
        buttonSave = findViewById(R.id.buttonSave)
        btnDelete = findViewById(R.id.buttonDelete)
        editTextTask = findViewById(R.id.editTextTask)
        editTextDescrpition = findViewById(R.id.editTextDescription)
        tvTime = findViewById(R.id.tvTime)
        tvDate = findViewById(R.id.tvDate)
        spinner = findViewById(R.id.spinner_priority_edit_schedule)
        btnExit = findViewById(R.id.imageButton6)
        recycler = findViewById(R.id.toDoRecyclerView)
        btnAddTodo = findViewById(R.id.abTodo)
        editTextTodo = findViewById(R.id.editTextAddTodo)
        autoComplete = findViewById(R.id.auto_complete_friends_edit_task)
        memberRecyclerView = findViewById(R.id.membersRecyclerView)
        btnAddFriends = findViewById(R.id.abMembers)
    }

    fun fetchData() {
        var data = intent.getParcelableExtra<TasksItem>("tasks")

        if(data!=null) {
            id = data.task!!.id!!
            editTextTask.setText(data.task!!.title)
            editTextDescrpition.setText(data.task!!.description)
            tvDate.text = data.task!!.date?.let { convertCalendar(it) }
            tvTime.text = data.task!!.time?.let { convertTime(it) }
            oldTitle = data.task!!.title.toString()
            updatedAt = data.task!!.updatedAt.toString()
            status = data.task!!.status
            setSpinner(data.task!!.priority)
            setTodo(data.todo as List<TodoItem>?)
            setFriends(data.member)
        }
    }

    private fun setFriendsDb() {
        for(j in dataFriends) {
            for (k in friendsDb) {
                if (j.id.toInt() == k) {
                    friendsString.add(j.username)
                }
            }
        }
    }

    private fun setFriends(member: List<MemberItem?>?) {
        if (member != null) {
            for(i in member) {
                if(i?.username != SessionManager(this).fetchUsername()) {
                    friendsDb.add(i?.id!!.toInt())
                } else {
                    userId = i?.id!!
                }
            }
        }
    }

    private fun addTodo() {
        if(editTextTodo.text.isEmpty()) {
            Toast.makeText(this, "Masukkan Todo", Toast.LENGTH_SHORT).show()
        } else {
            val name = editTextTodo.text.toString()
            val model = TodoItem(null, name)

            todosItem.add(model)
            todos.add(name)
            showRecyclistTodo()
        }
    }

    private fun setTodo(todo: List<TodoItem>?) {
        if (todo != null) {
            todosItem.addAll(todo)
            setTodoStatus()
        }
    }

    private fun setTodoStatus() {
        Log.d(TAG, "status task : $status")
        for(i in todosItem) {
            if(status == 1) {
                i.status = 1
                val model = TodoUpdate(i, status!!)
                todoItemUpdate.add(model)
            } else if(status == null) {
                break
            }
        }

        Log.d(TAG, "$todoItemUpdate")

        showRecyclistTodo()
    }

    private fun showRecyclistTodo() {
        val adapter = TodoAdapterEditDelete(todosItem)
        Log.d(TAG, "$todos")

        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.setOnClickDeleteTodo(object: TodoAdapterEditDelete.OnClickDeleteTodoEditDelete {
            override fun onClickItem(data: TodoItem) {
                todoItemDelete.add(data)
                todosItem.remove(data)
                todos.remove(data.name)

                showRecyclistTodo()
            }

        })

        adapter.setOnCheckedTodo(object: TodoAdapterEditDelete.OnCheckedTodo {
            override fun onCheckedItem(data: TodoItem, status: Boolean) {
                for(i in todosItem) {
                    if(data.id == i.id) {
                        if(status){
                            i.status = 1
                        } else {
                            i.status = 0
                        }
                    }
                }

                val model = TodoUpdate(data, checkNewStatusSchedule(status))
                val oldModel = TodoUpdate(data, checkStatusOldSchedule(status))

                todoItemUpdate.remove(oldModel)
                todoItemUpdate.add(model)
                Log.d(TAG, "todoitemupdate : $todoItemUpdate")
            }

        })
    }

    private fun checkNewStatusSchedule(status: Boolean): Int {
        return if (status) {
            1
        }
        else {
            0
        }
    }

    private fun checkStatusOldSchedule(status: Boolean): Int {
        return if(status) {
            0
        } else {
            1
        }
    }

    private fun updateTodo(data: ArrayList<TodoUpdate>) {
        val apiClient = APIClient()

        if(todoItemUpdate.isNotEmpty()) {
            for(i in todoItemUpdate) {
                if(i.todo.id != null) {
                    apiClient.getApiService(this).updateTodo(i.todo.id!!, i.todo.name!!, i.status)
                        .enqueue(object: Callback<UpdateTodoResponse> {
                            override fun onResponse(
                                call: Call<UpdateTodoResponse>,
                                response: Response<UpdateTodoResponse>
                            ) {
                                if(response.code() == 200) {
                                    Log.d(TAG, "update todo berhasil")
                                }
                            }

                            override fun onFailure(call: Call<UpdateTodoResponse>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }
        }
    }

    private fun deleteTodo(data: ArrayList<TodoItem>) {
        val apiClient = APIClient()

        if(todoItemDelete.isNotEmpty()) {
            for(i in todoItemDelete) {
                if(i.id != null) {
                    apiClient.getApiService(this).deleteTodo(i.id!!)
                        .enqueue(object : Callback<DeleteTodoResponse> {
                            override fun onResponse(
                                call: Call<DeleteTodoResponse>,
                                response: Response<DeleteTodoResponse>
                            ) {
                                if(response.code() == 200) {
                                    Log.d(TAG, "Berhasil delete todo")
                                }
                            }

                            override fun onFailure(call: Call<DeleteTodoResponse>, t: Throwable) {
                                Log.d(TAG, "$t")
                            }
                        })
                }
            }
        }
    }

    private fun createTodo() {
        val apiClient = APIClient()

        if(todos.isNotEmpty()) {
            apiClient.getApiService(this).createTodo(id, todos)
                .enqueue(object: Callback<CreateTodoResponse> {
                    override fun onResponse(
                        call: Call<CreateTodoResponse>,
                        response: Response<CreateTodoResponse>
                    ) {
                        if(response.code() == 200) {
                            Log.d(TAG, "create todo success")
                        }
                    }

                    override fun onFailure(call: Call<CreateTodoResponse>, t: Throwable) {
                        Log.d(TAG, "$t")
                    }

                })
        }
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
                        response.body()?.let {
                            dataFriends.addAll(it)
                        }
                        response.body()?.let { setAutoComplete(it) }
                        setFriendsDb()
                        setDataRecycler()
                    }
                }

                override fun onFailure(call: Call<List<GetFriendResponse>>, t: Throwable) {
                    Log.d(TAG, "error get Friends : $t")
                }

            })
    }

    private fun setDataRecycler() {
        for (i in dataFriends) {
            for (j in friendsDb) {
                if(j == i.id.toInt()) {
                    friends.add(i)
                    getImageUser(i, i.photo.toString())
                }
            }
        }
    }

    private fun getImageUser(data: GetFriendResponse, imageName: String) {
        val apiClient = APIClient()
        var bitmap: Bitmap

        apiClient.getApiService(this).getPhoto(imageName)
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.code() == 200) {
                        bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                        val model = ArrayFriendsTask(data, bitmap)
                        arrayRecycler.add(model)
//                        Log.d(TAG, "bitmap : ${bitmapArray.size}")
                        if(arrayRecycler.size == friends.size) {
                            showRecyclistFriends()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "Error : $t") }

            })
    }

    private fun showRecyclistFriends() {
        memberRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        memberRecyclerView.setHasFixedSize(true)

        val adapter = FriendsAdapterTask(arrayRecycler)
        memberRecyclerView.adapter = adapter

        adapter.setOnButtonClickCallback(object: FriendsAdapterTask.OnButtonCLickCallback {
            override fun onClickButton(data: ArrayFriendsTask) {
                friendsDb.remove(data.data.id.toInt())
                friends.remove(data.data)
                friendsString.remove(data.data.username)
                arrayRecycler.remove(data)
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
                    getImageUser(i, i.photo.toString())
                }
            }
        }
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

    private fun convertCalendar(date: String): String {
        dateSave = dateFormatDB.format(dateFormatDB.parse(date))

        return dateFormatView.format(dateFormatDB.parse(date))
    }

    private fun convertTime(time: String): String {
        timeSave = timeFormatDB.format(timeFormatDB.parse(time))

        return timeFormatView.format(timeFormatDB.parse(time))
    }

    private fun setSpinner(priority: String?) {
        priorityList = resources.getStringArray(R.array.priority)
        priorityListNumber = resources.getStringArray(R.array.priority_number)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, this.priorityList
        )

        spinner.adapter = adapter
        spinner.setSelection(priority!!.toInt())
    }

    fun cancelAlarm() {
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val id = getIdAlarm()
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id.toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        p.cancel(pendingIntent)
    }

    fun getIdAlarm(): Long{
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val timeUpdatedAt = format.parse(updatedAt).time
        var hasil = 0

        for(i in oldTitle) {
            hasil += i.code
        }

        return (timeUpdatedAt+hasil)
    }


    fun deleteTask() {
        val apiClient = APIClient()
        val intent = Intent(this, ListTaskActivity::class.java)

        apiClient.getApiService(this).deleteTask(id)
            .enqueue(object: Callback<DeleteTaskResponse> {
                override fun onResponse(
                    call: Call<DeleteTaskResponse>,
                    response: Response<DeleteTaskResponse>
                ) {
                    if(response.code().equals(202)) {
                        Toast.makeText(applicationContext, "task deleted successfully", Toast.LENGTH_SHORT).show()
                        cancelAlarm()
                        startActivity(intent)
                    } else if(response.code() == 401){
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        SessionManager(applicationContext).clearTokenAndUsername()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "task deleted failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeleteTaskResponse>, t: Throwable) {
                    Log.d("failed delete task", "$t")
                }

            })
    }

    fun saveTask() {
        val apiClient = APIClient()
        val title = editTextTask.text.toString()
        val intent = Intent(this, ListTaskActivity::class.java)
        val description = editTextDescrpition.text.toString()
        val priority = priorityListNumber[spinner.selectedItemPosition]

        friendsDb.add(userId)

        Log.d(TAG ,"todo saved : $todos")

        updateTodo(todoItemUpdate)
        deleteTodo(todoItemDelete)
        createTodo()

        apiClient.getApiService(this).updateTask(
            id,
            title,
            description,
            null,
            date = dateSave,
            time = timeSave,
            priority = priority.toInt(),
            friends = friendsDb
        )
            .enqueue(object: Callback<UpdateTaskResponse> {
                override fun onResponse(
                    call: Call<UpdateTaskResponse>,
                    response: Response<UpdateTaskResponse>
                ) {
                    if(response.code().equals(200)) {
                        Toast.makeText(applicationContext, "task updated successfully", Toast.LENGTH_SHORT).show()
                        cancelAlarm()
                        startActivity(intent)
                    } else if(response.code() == 401){
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        SessionManager(applicationContext).clearTokenAndUsername()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "task updated failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateTaskResponse>, t: Throwable) {
                    Log.d("failed update task", "$t")
                }

            })
    }

    fun toTaskActivity() {
        startActivity(Intent(this, ListTaskActivity::class.java))
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

        tvDate.text = "$savedDate-$savedMonth-$savedYear"
        dateSave = "$savedYear-$savedMonth-$savedDate"
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, ListTaskActivity::class.java))
        finish()
    }
}