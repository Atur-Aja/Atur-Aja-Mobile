package com.aturaja.aturaja.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.TaskAdapter
import com.aturaja.aturaja.model.GetAllTaskResponse2
import com.aturaja.aturaja.model.TasksItem
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.service.AlarmBroadcast
import com.aturaja.aturaja.session.SessionManager
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
    private var newArrayList = ArrayList<TasksItem>()
    private var arraySorting = ArrayList<TasksItem>()
    private val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val timeFormatDB = SimpleDateFormat("HH:mm:ss", Locale.US)
    private val TAG ="TaskActivity"


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
                                newArrayList.addAll(it.tasks)
                                loopAlarm(it.tasks)
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
        var date = ""
        var time = ""
        var idAlarm: Long

        for (i in tasks) {
            date = "${dateFormatDB.format(dateFormatDB.parse(i.task?.date))}"
            time = "${timeFormatDB.format(timeFormatDB.parse(i.task?.time))}"
            timeTask = dateTimeFormatAlarm.parse("$date $time")
            idAlarm = getIdAlarm(i.task?.updatedAt, i.task?.title)

            setAlarm(idAlarm, timeTask)
        }
    }

    private fun setAlarm(idAlarm: Long, timeTask: Date?) {
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
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

    private fun showRecyclist() {
        val adapter = TaskAdapter(arraySorting)
        var intent = Intent(this, EditDeleteTaskActivity::class.java)

        recyclerViewTask.adapter = adapter

        adapter.setOnTaskClickCallback(object: TaskAdapter.OnTaskClickCallback {
            override fun onTaskClicked(data: TasksItem) {
                intent.putExtra("tasks", data)
                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}