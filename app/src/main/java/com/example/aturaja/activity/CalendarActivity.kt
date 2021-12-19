package com.example.aturaja.activity

import android.app.AlarmManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.ScheduleAdapter
import com.example.aturaja.model.GetAllScheduleResponse2
import com.example.aturaja.model.SchedulesItem
import com.example.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import android.app.PendingIntent
import android.os.Build
import com.example.aturaja.service.AlarmBroadcast


class CalendarActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private var newArrayList = ArrayList<SchedulesItem>()
    private val TAG = "CalendarActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        newRecyclerView = findViewById(R.id.scheduleRecyclerView)

        getUserData()
        Log.d(TAG, "created")
    }

    private fun getUserData() {
        var apiClient = APIClient()

        apiClient.getApiService(this).getSchedules()
            .enqueue(object: Callback<GetAllScheduleResponse2> {
                override fun onResponse(
                    call: Call<GetAllScheduleResponse2>,
                    response: Response<GetAllScheduleResponse2>
                ) {
                    response.body()?.let {
                        if(it.schedules != null) {
                            newArrayList.addAll(it.schedules)
                            showRecyclist(it.schedules)
                            countNotifAndInterval(it.schedules)
                        }
                    }
                }

                override fun onFailure(call: Call<GetAllScheduleResponse2>, t: Throwable) {
                    Log.d("calendar", "$t")
                }
            })
    }

    private fun countNotifAndInterval(schedules: List<SchedulesItem>) {
        var date = ""
        var time = ""
        var interval: String
        var notif: Long
        var idAlarm: Long
        var timeSchedule: Date
        val timeFormatDB = SimpleDateFormat("HH:mm:ss", Locale.US)
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateTimeFormatAlarm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        for( i in schedules) {
            date = "${dateFormatDB.format(dateFormatDB.parse(i.schedule?.date))}"
            time = "${timeFormatDB.format(timeFormatDB.parse(i.schedule?.startTime))}"
            timeSchedule = dateTimeFormatAlarm.parse("${date} ${time}")
            notif = countNotfif(i.schedule?.notification)
            idAlarm = getIdAlarm(i.schedule?.updatedAt, i.schedule?.title)
            interval = i.schedule?.repeat.toString()

            intervalSetter(timeSchedule, notif, idAlarm, interval)
        }
    }

    private fun setAlarmWithInterval(timeScheduleAlarm: Date?, notifAlarm: Long, id: Long, interval: Long) {
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        val pendingIntentChecker = PendingIntent.getBroadcast(
            applicationContext,
            id.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        val pendingIntent = PendingIntent.getBroadcast(
            this, id.toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmUp = pendingIntentChecker != null

        Log.d(TAG, "tanggal di interval: $timeScheduleAlarm")
        Log.d(TAG, "notif di interval : $notifAlarm")
        Log.d(TAG, "id di interval : $id")

        if(timeScheduleAlarm != null) {
            if(alarmUp) {
                Log.d(TAG, "ada")
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    p.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        timeScheduleAlarm.time - notifAlarm,
                        AlarmManager.INTERVAL_DAY * interval,
                        pendingIntent
                    )
                } else {
                    p.setRepeating(AlarmManager.RTC_WAKEUP,
                        timeScheduleAlarm.time - notifAlarm,
                        AlarmManager.INTERVAL_DAY * interval,
                        pendingIntent)
                }
            }
        }
    }

    private fun setAlarmWithoutInterval(timeScheduleAlarm: Date?, notif: Long, idAlarm: Long) {
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        val pendingIntentChecker = PendingIntent.getBroadcast(
            applicationContext,
            idAlarm.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        val pendingIntentSet = PendingIntent.getBroadcast(
            applicationContext,
            idAlarm.toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmUp = pendingIntentChecker != null
        Log.d(TAG, "tanggal tanpa interval : $timeScheduleAlarm")
        Log.d(TAG, "notif tanpa interval : $notif")
        Log.d(TAG, "id alarm : $idAlarm")

        if(timeScheduleAlarm != null) {
            if(alarmUp) {
                Log.d(TAG, "ada")
            } else {
                Log.d(TAG, "tidak ada")
                Log.d(TAG, "milis : ${timeScheduleAlarm.time - notif}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    p.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        timeScheduleAlarm.time - notif,
                        pendingIntentSet
                    )
                } else {
                    p.set(AlarmManager.RTC_WAKEUP,
                        timeScheduleAlarm.time - notif,
                        pendingIntentSet
                    )
                }
            }
        }

        if(pendingIntentSet!= null) {
            Log.d(TAG, "berhasil")
        } else {
            Log.d(TAG, "gagal")
        }
    }

    private fun intervalSetter(timeSchedule: Date?, notif: Long, idAlarm: Long, interval: String) {
        if(interval == "never") {
            setAlarmWithoutInterval(timeSchedule, notif, idAlarm)
        }else if (interval == "weekly") {
            setAlarmWithInterval(timeSchedule, notif, idAlarm, 7)
        } else {
            setAlarmWithInterval(timeSchedule, notif, idAlarm, 30)
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

    private fun countNotfif(notification: String?): Long{
        val notifTime: Long

        Log.d(TAG, "$notification")

        if(notification == "5 minutes") {
            notifTime = TimeUnit.MINUTES.toMillis(5)
        } else if(notification == "10 minutes") {
            notifTime = TimeUnit.MINUTES.toMillis(10)
        } else {
            notifTime = TimeUnit.MINUTES.toMillis(15)
        }

        return notifTime
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
