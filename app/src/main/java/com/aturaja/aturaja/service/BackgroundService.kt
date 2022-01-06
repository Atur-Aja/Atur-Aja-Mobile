package com.aturaja.aturaja.service

import android.app.*
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.*
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import com.aturaja.aturaja.R
import com.aturaja.aturaja.activity.HomeActivity
import com.aturaja.aturaja.model.DetailItem
import com.robertohuertas.endless.Actions
import com.robertohuertas.endless.ServiceState
import com.robertohuertas.endless.setServiceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import log
import java.lang.Exception
import java.util.*

//class BackgroundService: Service() {
//    private val timer = Timer()
//    var itemList = ArrayList<DetailItem>()
//    private var data = ArrayList<DetailItem>()
//    private var wm: WindowManager? = null
//    private lateinit var view: View
//    private lateinit var windowManager: WindowManager
//    var TAG = "ServiceStartYes"
//
//    override fun onBind(p0: Intent?): IBinder? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        startService(Intent(this, BackgroundService::class.java))
//    }
//
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
//    {
//        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0)
//        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)
////        itemList = intent.getStringArrayListExtra("appName") as ArrayList<String>
//        var data = intent.getParcelableArrayListExtra<DetailItem>("appName")
//        Log.d(TAG, "data intent : $data")
//        if (data != null) {
//            itemList = data as ArrayList<DetailItem>
//        }
//        Log.d(TAG, "data itemList : $itemList")
//
//        return START_STICKY
//    }
//
//    override fun onDestroy()
//    {
//        timer.cancel()
//
//        super.onDestroy()
//    }
//
//    private fun floatButton() {
//        val metrics = applicationContext.resources.displayMetrics
//        val layoutTape: Int
//
//        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        view = LayoutInflater.from(this).inflate(R.layout.floating_layout_2, null, false)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            layoutTape = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//        } else {
//            layoutTape = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE
//        }
//
//        val layoutParams = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            layoutTape,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            PixelFormat.TRANSLUCENT
//        )
//
//        layoutParams.gravity = Gravity.CENTER
//        layoutParams.x = 0
//        layoutParams.y = 0
//        layoutParams.width = metrics.widthPixels
//        layoutParams.height = metrics.heightPixels
//
//        view.findViewById<Button>(R.id.button_back).setOnClickListener {
//            val intent = Intent(applicationContext, HomeActivity::class.java)
////            val intent2 = Intent(applicationContext, BackgroundService::class.java)
//            intent.putExtra("appName", itemList)
//            startActivity(intent)
////            startService(intent2)
//            windowManager.removeViewImmediate(view)
//        }
//
//
//        windowManager.addView(view, layoutParams)
//    }
//
//
//    private inner class TimeTask(private var time: Double) : TimerTask()
//    {
//        override fun run()
//        {
//            check(getForegroundApp2())
//            Log.d(TAG, "${getForegroundApp2()}")
//        }
//    }
//
//    fun getForegroundApp2() :String{
//        var currentApp = "NULL"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val usm =
//                this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
//            val time = System.currentTimeMillis()
//            val appList =
//                usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
//            if (appList != null && appList.size > 0) {
//                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap<Long, UsageStats>()
//                for (usageStats in appList) {
//                    mySortedMap.put(usageStats.lastTimeUsed, usageStats)
//                }
//                if (mySortedMap != null && !mySortedMap.isEmpty()) {
//                    currentApp = mySortedMap.get(mySortedMap.lastKey())?.getPackageName() ?: null.toString()
//                }
//            }
//        } else {
//            val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//            val tasks = am.runningAppProcesses
//            currentApp = tasks[0].processName
//        }
//
//        return currentApp
//    }
//
//    fun check(app: String) {
//        for(name in itemList) {
//            try {
//                if(name.packageName == app) {
//                    backToApp()
//                }
//            } catch (e: Exception) {
//                Log.d(TAG, "gagal : $e")
//            }
//
//        }
//    }
//
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        super.onTaskRemoved(rootIntent)
//        sendBroadcast(Intent("startService"))
//    }
//
//    fun backToApp() {
//        try{
//            startActivity(Intent(this, HomeActivity::class.java))
//        } catch (e: Exception) {
//            Log.d(TAG, "gagal : $e")
//        }
//
//    }
//
//    companion object
//    {
//        const val TIMER_SERVICE = "com.example.aturaja"
//        const val TIME_EXTRA = "timeExtra"
//    }
//}

class BackgroundService : Service() {
    private var isServiceStarted = false
    private val timer = Timer()
    private val TAG = "ServiceBackground"
    private var wm: WindowManager? = null
    lateinit var view: View
    lateinit var windowManager: WindowManager
    private lateinit var window: Window
    var appName = ArrayList<DetailItem>()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            appName = intent.getParcelableArrayListExtra("appName")!!
            val action = intent.action
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
            }
        }

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, BackgroundService::class.java).also {
            it.setPackage(packageName)
        }
        val stopService = Intent(applicationContext, BackgroundService::class.java).also {
            it.setPackage(packageName)
            it.action = Actions.STOP.name
        }
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent)
        stopService()
    }

    private fun startService() {
        if (isServiceStarted) return

        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    timer.scheduleAtFixedRate(TimeTask(0.0), 0, 1000)
                }
                delay(1 * 60 * 1000)
            }
        }
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        override fun run()
        {
            check(getForegroundApp2())
            Log.d(TAG, "${getForegroundApp2()}")
        }
    }

    fun check(app: String) {
        try {
            for(i in appName) {
                if(i.packageName == app) {
                    if(Looper.myLooper() == null) {
                        Looper.prepare()
                        floatButton()
                        Looper.loop()
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "gagal : $e")
        }
    }

    private fun floatButton() {
        val metrics = applicationContext.resources.displayMetrics
        val layoutTape: Int
        val restartService = Intent(this, BackgroundService::class.java).also {
            it.setPackage(packageName)
            it.action = Actions.START.name
            it.putExtra("appName", appName)
        }

        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        view = LayoutInflater.from(this).inflate(R.layout.floating_layout, null, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutTape = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutTape = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE
        }

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutTape,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.OPAQUE
        )

        layoutParams.gravity = Gravity.CENTER
        layoutParams.x = 0
        layoutParams.y = 0
        layoutParams.width = metrics.widthPixels
        layoutParams.height = metrics.heightPixels

        view.findViewById<Button>(R.id.button_back).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            windowManager.removeViewImmediate(view)
            view.invalidate()


            this.startActivity(intent)

            val handler = Handler()

            stopService()
            handler.postDelayed({
                startService(restartService)
            }, 4000)
        }

        windowManager.addView(view, layoutParams)
    }

    fun getForegroundApp2() :String{
        var currentApp = "NULL"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usm =
                this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val appList =
                usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
            if (appList != null && appList.size > 0) {
                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap<Long, UsageStats>()
                for (usageStats in appList) {
                    mySortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap[mySortedMap.lastKey()]?.packageName ?: null.toString()
                }
            }
        } else {
            val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val tasks = am.runningAppProcesses
            currentApp = tasks[0].processName
        }

        return currentApp
    }

    private fun stopService() {
        try {
            stopForeground(true)
            stopSelf()
            stopService(Intent(this, BackgroundService::class.java))
        } catch (e: Exception) {
            Toast.makeText(this, "Focus mode had already been Stopped", Toast.LENGTH_SHORT).show()
        }
        isServiceStarted = false
        timer.cancel()
        setServiceState(this, ServiceState.STOPPED)
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Mode Fokus",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Anda Sedang Dalam Mode Fokus"
                it.enableLights(true)
                it.lightColor = Color.RED
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, BackgroundService::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle("Mode Fokus")
            .setContentText("Anda sedang dalam Mode Fokus")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_logo)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }
}
