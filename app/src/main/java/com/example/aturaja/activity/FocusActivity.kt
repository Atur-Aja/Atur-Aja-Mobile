package com.example.aturaja.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aturaja.adapter.AppListAdapter
import com.example.aturaja.databinding.ActivityFocusBinding
import com.example.aturaja.model.AppItem
import com.example.aturaja.model.DetailItem
import com.example.aturaja.service.BackgroundService
import com.example.aturaja.session.SessionManager

class FocusActivity : AppCompatActivity() {
    var TAG = "Main"
    var appItem = ArrayList<AppItem>()
    var appName = ArrayList<DetailItem>()
    var percobaan = ArrayList<DetailItem>()


    var timerStarted = false
    lateinit var binding: ActivityFocusBinding
    lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        percobaan.add(DetailItem("CPU-Z", "com.cpuid.cpu_z"))
        percobaan.add(DetailItem("Chrome", "com.android.chrome"))

        setStatusService()
        getData()
        showRecyclerList()

        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))


        binding.buttonStart.setOnClickListener {
                startStopTimer()
        }

        binding.buttonStop.setOnClickListener{
            stopTimer()
        }

        serviceIntent = Intent(applicationContext, BackgroundService::class.java)
//        registerReceiver(updateTime, IntentFilter(BackgroundService.TIMER_SERVICE))
    }

    fun setStatusService() {
        timerStarted = SessionManager(this).fetchStatusService()
        if(timerStarted) {
//            binding.buttonStart.text = "Stop"
            binding.buttonStart.text = "Stop"
            binding.buttonStop.visibility = View.VISIBLE
            binding.buttonStop.visibility = View.GONE
        } else {
            binding.buttonStart.text = "Start"
            binding.buttonStart.visibility = View.VISIBLE
            binding.buttonStop.visibility = View.GONE
        }
    }

    private fun startStopTimer() {
        if(timerStarted) {
            stopTimer()
        } else {
            if(appName.isEmpty()) {
                Toast.makeText(this, "Pilih aplikasi ", Toast.LENGTH_LONG).show()
            } else {
                startTimer()
            }
        }
    }

    private fun startTimer()
    {
        if(appName.isEmpty()) {
            Toast.makeText(this, "pilih aplikasi", Toast.LENGTH_SHORT).show()
        } else {
            SessionManager(this).saveStatusService(true)
            serviceIntent.putExtra(BackgroundService.TIME_EXTRA, time)
            serviceIntent.putExtra("appName", appName)
            startService(serviceIntent)
            binding.buttonStart.text = "Stop"
            binding.buttonStart.visibility = View.GONE
            binding.recycler.visibility = View.GONE
            binding.buttonStop.visibility = View.VISIBLE
            timerStarted = SessionManager(this).fetchStatusService()
        }
    }

    private fun stopTimer()
    {
        SessionManager(this).saveStatusService(false)
        stopService(serviceIntent)
        binding.buttonStart.text = "Start"
        binding.recycler.visibility = View.VISIBLE
        binding.buttonStart.visibility = View.VISIBLE
        binding.buttonStop.visibility = View.GONE
        timerStarted = SessionManager(this).fetchStatusService()
    }

//
//    private val br: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            updateGUI(intent)
//        }
//    }

    override fun onPause() {
        super.onPause()
//        unregisterReceiver(updateTime)
        Log.i(TAG, "Unregistered broacast receiver")
    }

    override fun onStop() {
        try {
//            unregisterReceiver(updateTime)
        } catch (e: Exception) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "Stopped service")
        binding.buttonStart.text = "Start"
//        SessionManager(this).saveStatusService(false)
//        timerStarted = SessionManager(this).fetchStatusService()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
//        registerReceiver(updateTime, IntentFilter(BackgroundService.TIMER_SERVICE))
        Log.i(TAG, "Registered broacast receiver")
    }

    fun getData() {
        val app = getAllPackages()

        appItem.clear()
        appItem.addAll(app.map {
            val itemDetail = DetailItem(
                this.packageManager.getApplicationLabel(it.packageName),
                it.packageName
            )
            val item = AppItem(
                this.packageManager.getApplicationIcon(it),
                itemDetail
//                this.packageManager.getApplicationLabel(it.packageName),
//                it.packageName,
//                this.packageManager.getApplicationIcon(it)
            )
            item })
    }

    private fun getAllPackages():List<ApplicationInfo> {
        val pm = packageManager
        return packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN, null).addCategory(
                Intent.CATEGORY_LAUNCHER), 0)
            .filter { it.activityInfo.packageName != this.packageName}
            .distinctBy { it.activityInfo.packageName }
            .map { pm.getApplicationInfo(it.activityInfo.packageName, 0) }
            .sortedBy { pm.getApplicationLabel(it.packageName) }
    }

    fun PackageManager.getApplicationLabel(packageName: String): String {
        val info = getApplicationInfo(packageName, 0)
        return getApplicationLabel(info).toString()
    }

    fun showRecyclerList() {
        Log.d(TAG, "percobaan $percobaan")
        val listAppAdapter = AppListAdapter(appItem)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = listAppAdapter
        appName.clear()

        listAppAdapter.setOnSwitchCheckedCallback(object: AppListAdapter.OnSwitchCheckedCallback {
            override fun onSwitchChecked(status: Boolean, data: DetailItem) {
                if(status) {
                    appName.add(data)
                    Toast.makeText(applicationContext, "added ${data.name}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "$appName")
                } else {
                    appName.remove(data)
                    Toast.makeText(applicationContext, "remove ${data.name}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "$appName")
                }
            }

        })
    }
}