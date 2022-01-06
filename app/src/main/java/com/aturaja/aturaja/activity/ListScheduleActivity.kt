package com.aturaja.aturaja.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.ScheduleAdapter
import com.aturaja.aturaja.model.GetAllScheduleResponse2
import com.aturaja.aturaja.model.SchedulesItem
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.service.AlarmBroadcast
import com.aturaja.aturaja.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ListScheduleActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var recyler: RecyclerView
    private lateinit var btnAdd: ImageButton
    private val TAG = "ListSchedule"
    private var title = ""
    private var timeStart = ""
    private var timeEnd = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_schedule)

        initComponenet()
        getSchedule()
        btnBack.setOnClickListener {
            backToHome()
        }
        btnAdd.setOnClickListener {
            addSchedule()
        }
    }

    private fun initComponenet() {
        btnBack = findViewById(R.id.imageButton)
        recyler = findViewById(R.id.recycler_list_schedule)
        btnAdd = findViewById(R.id.add_list_schedule)
    }

    private fun getSchedule() {
        var apiClient = APIClient()

        apiClient.getApiService(this).getSchedules()
            .enqueue(object: Callback<GetAllScheduleResponse2> {
                override fun onResponse(
                    call: Call<GetAllScheduleResponse2>,
                    response: Response<GetAllScheduleResponse2>
                ) {
                    if(response.code() == 200) {
                        response.body()?.let {
                            if(it.schedules != null) {
                                showRecyclist(it.schedules)
                                countNotifAndInterval(it.schedules)
                            }
                        }
                    }else if(response.code() == 401){
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        SessionManager(applicationContext).clearTokenAndUsername()
                        finish()
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
            title = i.schedule?.title.toString()
            timeStart = i.schedule?.startTime.toString()
            timeEnd = i.schedule?.endTime.toString()
            date = "${dateFormatDB.format(dateFormatDB.parse(i.schedule?.date))}"
            time = "${timeFormatDB.format(timeFormatDB.parse(i.schedule?.startTime))}"
            timeSchedule = dateTimeFormatAlarm.parse("${date} ${time}")
            notif = countNotfif(i.schedule?.notification)
            idAlarm = getIdAlarm(i.schedule?.updatedAt, i.schedule?.title)
            interval = i.schedule?.repeat.toString()

            intervalSetter(timeSchedule, notif, idAlarm, interval)
        }
    }

    private fun intervalSetter(timeSchedule: Date?, notif: Long, idAlarm: Long, interval: String) {
        if(interval == "never") {
            setAlarmWithoutInterval(timeSchedule, notif, idAlarm)
        }
        else if (interval == "weekly") {
            setAlarmWithInterval(timeSchedule, notif, idAlarm, 7)
        } else {
            setAlarmWithInterval(timeSchedule, notif, idAlarm, 30)
        }
    }

    private fun setAlarmWithoutInterval(timeScheduleAlarm: Date?, notif: Long, idAlarm: Long) {
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        intent.putExtra("title", title)
        intent.putExtra("startTime", timeStart)
        intent.putExtra("endTime", timeEnd)
        val cal = Calendar.getInstance()
        val dateTimeFormatAlarm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val calen = dateTimeFormatAlarm.parse(dateTimeFormatAlarm.format(cal.timeInMillis))
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
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmUp = pendingIntentChecker != null
        Log.d(TAG, "tanggal tanpa interval : $timeScheduleAlarm")
        Log.d(TAG, "notif tanpa interval : $notif")
        Log.d(TAG, "id alarm : $idAlarm")
        Log.d(TAG, "cal $calen")

        val bol : Boolean = dateTimeFormatAlarm.parse(dateTimeFormatAlarm.format(cal.time)) >= timeScheduleAlarm

        Log.d(TAG, "$bol")

        if(calen.time >= timeScheduleAlarm?.time!! - notif) {
            Log.d(TAG, "tidak buat alarm")
        } else {
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
                        p.set(
                            AlarmManager.RTC_WAKEUP,
                            timeScheduleAlarm.time - notif,
                            pendingIntentSet
                        )
                    }
                }
            }
        }

        if(pendingIntentSet!= null) {
            Log.d(TAG, "berhasil")
        } else {
            Log.d(TAG, "gagal")
        }
    }

    private fun setAlarmWithInterval(timeScheduleAlarm: Date?, notifAlarm: Long, id: Long, interval: Int) {
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        intent.putExtra("title", title)
        intent.putExtra("startTime", timeStart)
        intent.putExtra("endTime", timeEnd)
        val cal = Calendar.getInstance()
        val dateTimeFormatAlarm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val calen = dateTimeFormatAlarm.parse(dateTimeFormatAlarm.format(cal.timeInMillis))
        val pendingIntentChecker = PendingIntent.getBroadcast(
            applicationContext,
            id.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        val pendingIntentSet = PendingIntent.getBroadcast(
            applicationContext,
            id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmUp = pendingIntentChecker != null


        if(calen.time >= (timeScheduleAlarm?.time!! - notifAlarm)) {
            Log.d(TAG, "not make alarm")
        } else {
            if(timeScheduleAlarm != null) {
                if(alarmUp) {
                    Log.d(TAG, "$id")
                    Log.d(TAG, "ada")
                    Log.d(TAG, "$alarmUp")
                } else {
                    Log.d(TAG, "$id")
                    Log.d(TAG, "$alarmUp")
                    Log.d(TAG, "make alarm")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        p.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP,
                            timeScheduleAlarm.time - notifAlarm,
                            AlarmManager.INTERVAL_DAY * interval,
                            pendingIntentSet
                        )
                    } else {
                        p.setRepeating(AlarmManager.RTC_WAKEUP,
                            timeScheduleAlarm.time - notifAlarm,
                            AlarmManager.INTERVAL_DAY * interval,
                            pendingIntentSet
                        )
                    }
                }
            }
        }
    }

    private fun countNotfif(notification: String?): Long{
        val notifTime: Long

        if(notification == "5 minutes") {
            notifTime = TimeUnit.MINUTES.toMillis(5)
        } else if(notification == "10 minutes") {
            notifTime = TimeUnit.MINUTES.toMillis(10)
        } else {
            notifTime = TimeUnit.MINUTES.toMillis(15)
        }

        return notifTime
    }

    private fun getIdAlarm(updatedAt: String?, title: String?): Long {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        var hasilTitle = 0
        for(i in title!!) {
            hasilTitle += i.code
        }

        return hasilTitle + format.parse(updatedAt).time
    }

    private fun showRecyclist(schedules: List<SchedulesItem>) {
        val intent = Intent(this, EditDeleteSchedule::class.java)

        recyler.setHasFixedSize(true)
        recyler.layoutManager = LinearLayoutManager(this)


        val scheduleListAdapter = ScheduleAdapter(schedules as ArrayList<SchedulesItem>)
        recyler.adapter = scheduleListAdapter

        scheduleListAdapter.setOnItemClickCallback(object : ScheduleAdapter.OnItemClickCallback {
            override fun onClickItem(data: SchedulesItem) {
                intent.putExtra("schedule_data", data)
                startActivity(intent)
            }
        })
    }

    private fun backToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun addSchedule() {
        startActivity(Intent(this, AddScheduleActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}