package com.aturaja.aturaja.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.TaskAdapter
import com.aturaja.aturaja.model.*
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.service.AlarmBroadcast
import com.aturaja.aturaja.session.SessionManager
import com.google.android.material.progressindicator.LinearProgressIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListTaskActivity : AppCompatActivity() {
    private lateinit var recyclerViewTask: RecyclerView
    private lateinit var buttonAdd: ImageButton
    private lateinit var buttonExit: ImageButton
    private lateinit var progress: LinearProgressIndicator
    private var newArrayList = ArrayList<TasksItem>()
    private var arraySorting = ArrayList<TasksItem>()
    private val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val timeFormatDB = SimpleDateFormat("HH:mm:ss", Locale.US)
    private val timeFormatAlarm = SimpleDateFormat("HH:mm", Locale.US)
    private val TAG ="TaskActivity"
    private var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        InitComponent()

        recyclerViewTask.layoutManager = LinearLayoutManager(this)
        recyclerViewTask.setHasFixedSize(true)

        getData()
        buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        buttonExit.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun InitComponent() {
        recyclerViewTask = findViewById(R.id.taskRecyclerView)
        buttonAdd = findViewById(R.id.imageButton2)
        buttonExit = findViewById(R.id.imageButton)
        progress = findViewById(R.id.progerss_bar)
    }

    fun getData() {
        val apiClient = APIClient()


        apiClient.getApiService(this).getTask()
            .enqueue(object: Callback<GetAllTaskResponse2> {
                override fun onResponse(
                    call: Call<GetAllTaskResponse2>,
                    response: Response<GetAllTaskResponse2>
                ) {
                    if(response.code() == 200) {
                        response.body()?.let {
                            if(it.tasks != null) {
                                newArrayList.clear()
                                arraySorting.clear()
                                newArrayList.addAll(it.tasks)
                                loopAlarm(it.tasks)
                                recyclerViewTask.visibility = View.VISIBLE
                                progress.visibility = View.GONE
//                                setCheckedTask()
                                setCheckedTodo()
                                sortingTask()
                            }
                        }
                    } else if(response.code() == 401){
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        SessionManager(applicationContext).clearTokenAndUsername()
                        finish()
                    }
                }

                override fun onFailure(call: Call<GetAllTaskResponse2>, t: Throwable) {

                }

            })
    }

    private fun loopAlarm(tasks: List<TasksItem>) {
        val dateTimeFormatAlarm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        var timeTask: Date
        var title = ""
        var startTime = ""
        var date = ""
        var time = ""
        var idAlarm: Long

        for (i in tasks) {
            date = "${dateFormatDB.format(dateFormatDB.parse(i.task?.date))}"
            time = "${timeFormatDB.format(timeFormatDB.parse(i.task?.time))}"
            title = i.task?.title.toString()
            startTime = i.task?.time.toString()
            timeTask = dateTimeFormatAlarm.parse("$date $time")
            idAlarm = getIdAlarm(i.task?.updatedAt, i.task?.title)

            setAlarm(idAlarm, timeTask, title, startTime)
        }
    }

    private fun setAlarm(idAlarm: Long, timeTask: Date?, title: String, startTime: String) {
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        intent.putExtra("title", title)
        intent.putExtra("startTime", timeFormatAlarm.format(timeFormatDB.parse(startTime)))
        val cal = Calendar.getInstance()
        val dateTimeFormatAlarm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val bol : Boolean = dateTimeFormatAlarm.parse(dateTimeFormatAlarm.format(cal.time)) >= timeTask
        val pendingIntentChecker = PendingIntent.getBroadcast(
            applicationContext,
            idAlarm.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        val pendingIntent = PendingIntent.getBroadcast(
            this, idAlarm.toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmUp = pendingIntentChecker != null

        if(!bol) {
            if(timeTask != null) {
                if(alarmUp) {
                    Log.d(TAG, "alarm up : $alarmUp")
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        p.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            timeTask.time,
                            pendingIntent
                        )
                    } else {
                        p.set(AlarmManager.RTC_WAKEUP,
                            timeTask.time,
                            pendingIntent
                        )
                    }
                }
            }
        }
    }

    private fun getIdAlarm(updatedAt: String?, title: String?): Long {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        var hasilTitle = 0
        for(i in title!!) {
            hasilTitle += i.code
        }

        return hasilTitle + format.parse(updatedAt).time
    }

    private fun sortingTask() {
        arraySorting.clear()

        for(iter in 3 downTo 0) {
            for(j in newArrayList) {
                if(j.task?.priority == "$iter") {
                    arraySorting.add(j)
                }
            }
        }

        showRecyclist()
    }

    private fun setCheckedTodo() {
        var status: Boolean
        var status2: Boolean
        for(i in newArrayList) {
            if (i.todo != null) {
                for (j in i.todo) {
                    status = j?.status == 1
                    if(status) {
                        i.task?.status = 1
                    } else {
                        i.task?.status = 0
                        break
                    }
                    count++
                }
            }
        }
    }


    private fun showRecyclist() {
        val adapter = TaskAdapter(arraySorting)
        var intent = Intent(this, EditDeleteTaskActivity::class.java)
        var convertBool: Int

        recyclerViewTask.adapter = adapter



        if(progress.isVisible == false) {
            adapter.setOnTaskClickCallback(object: TaskAdapter.OnTaskClickCallback {
                override fun onTaskClicked(data: TasksItem) {
                    intent.putExtra("tasks", data)
                    startActivity(intent)
                }
            })

            adapter.setOnCheckedTas(object: TaskAdapter.OnCheckedTask {
                override fun onTaskChecked(data: TasksItem, status: Boolean) {

                    if(status) {
                        convertBool = 1
                    } else {
                        convertBool = 0
                    }
                    progress.visibility = View.VISIBLE
                    recyclerViewTask.visibility = View.GONE
                    checkTodoTaskAdapter(data, convertBool)
                }

            })
        }
    }

    private fun checkTodoTaskAdapter(data: TasksItem, convertBool: Int) {
        if(data.todo != null) {
            for(i in data.todo) {
                try {
                    if(convertBool == 1) {
                        i?.status = 1
                        updateTodo(i, convertBool, data)
                    } else {
                        i?.status = 0
                        updateTodo(i, convertBool, data)
                    }
                    count++
                }catch (e: Exception) {
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                }
            }
        }

            onCheckedTask(data, convertBool)
    }

    private fun updateTodo(i: TodoItem?, convertBool: Int, data: TasksItem) {
        val apiClient = APIClient()

                if(i?.id != null) {
                    apiClient.getApiService(this).updateTodo(i.id, i.name.toString(), i.status!!)
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


    private fun onCheckedTask(data: TasksItem, status: Int) {
        val apiClient = APIClient()
        val friends = getFriendsDb(data.member)

        apiClient.getApiService(this).updateTask(
            data.task?.id!!.toInt(),
            data.task.title.toString(),
            null,
            status,
            data.task.date.toString(),
            timeFormatAlarm.format(timeFormatDB.parse(data.task.time)),
            data.task.priority!!.toInt(),
            friends
        ).enqueue(object: Callback<UpdateTaskResponse> {
            override fun onResponse(
                call: Call<UpdateTaskResponse>,
                response: Response<UpdateTaskResponse>
            ) {
                if(response.code() == 200) {
                    getData()
                }
            }

            override fun onFailure(call: Call<UpdateTaskResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getFriendsDb(priority: List<MemberItem?>?): ArrayList<Int> {
        val hasil = ArrayList<Int>()

        if (priority != null) {
            for(i in priority) {
                if (i != null) {
                    i.id?.let { hasil.add(it.toInt()) }
                }
            }
        }

        return hasil
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}