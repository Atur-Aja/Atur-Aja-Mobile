package com.example.aturaja.service

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.aturaja.model.DetailItem
import java.lang.Exception
import java.util.*

class BackgroundService: Service() {
    private val timer = Timer()
    var itemList = ArrayList<DetailItem>()
    private var data = ArrayList<DetailItem>()
    var TAG = "ServiceStartYes"

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0)
        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)
//        itemList = intent.getStringArrayListExtra("appName") as ArrayList<String>
        var data = intent.getParcelableArrayListExtra<DetailItem>("appName")
        Log.d(TAG, "data intent : $data")
        if (data != null) {
            itemList = data as ArrayList<DetailItem>
        }
        Log.d(TAG, "data itemList : $itemList")

        return START_STICKY
    }

    override fun onDestroy()
    {
        timer.cancel()

        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        override fun run()
        {
            check(getForegroundApp2())
            Log.d(TAG, "${getForegroundApp2()}")
        }
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
                    mySortedMap.put(usageStats.lastTimeUsed, usageStats)
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey())?.getPackageName() ?: null.toString()
                }
            }
        } else {
            val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val tasks = am.runningAppProcesses
            currentApp = tasks[0].processName
        }

        return currentApp
    }

    fun check(app: String) {
        for(name in itemList) {
            try {
                if(name.packageName == app) {
                    backToApp()
                }
            } catch (e: Exception) {
                Log.d(TAG, "gagal : $e")
            }

        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        sendBroadcast(Intent("startService"))
    }

    fun backToApp() {
        val startMain = Intent(packageManager.getLaunchIntentForPackage("com.example.aturaja"))

        try{
            startActivity(startMain)
        } catch (e: Exception) {
            Log.d(TAG, "gagal : $e")
        }

    }

    companion object
    {
        const val TIMER_SERVICE = "com.example.aturaja"
        const val TIME_EXTRA = "timeExtra"
    }
}