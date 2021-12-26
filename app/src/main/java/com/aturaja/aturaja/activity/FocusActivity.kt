package com.aturaja.aturaja.activity

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aturaja.aturaja.adapter.AppListAdapter
import com.aturaja.aturaja.databinding.ActivityFocusBinding
import com.aturaja.aturaja.model.AppItem
import com.aturaja.aturaja.model.DetailItem
import com.aturaja.aturaja.service.BackgroundService
import com.aturaja.aturaja.session.SessionManager
import android.app.AppOpsManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.robertohuertas.endless.Actions
import com.robertohuertas.endless.ServiceState
import com.robertohuertas.endless.getServiceState
import log


//class FocusActivity : AppCompatActivity() {
//    var TAG = "Main"
//    var appItem = ArrayList<AppItem>()
//    var appName = ArrayList<DetailItem>()
//    var percobaan = ArrayList<DetailItem>()
//
//
//    var timerStarted = false
//    lateinit var binding: ActivityFocusBinding
//    lateinit var serviceIntent: Intent
//    private var time = 0.0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityFocusBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        percobaan.add(DetailItem("CPU-Z", "com.cpuid.cpu_z"))
//        percobaan.add(DetailItem("Chrome", "com.android.chrome"))
//
//        setStatusService()
//        getData()
//        showRecyclerList()
//
//
//        binding.buttonStart.setOnClickListener {
//            if(checkPrermission()) {
//                startStopTimer()
//            } else {
//                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
//            }
//        }
//
//        binding.buttonStop.setOnClickListener{
//            stopTimer()
//        }
//
//        serviceIntent = Intent(applicationContext, BackgroundService::class.java)
////        registerReceiver(updateTime, IntentFilter(BackgroundService.TIMER_SERVICE))
//    }
//
//    fun setStatusService() {
//        timerStarted = SessionManager(this).fetchStatusService()
//        if(timerStarted) {
////            binding.buttonStart.text = "Stop"
//            binding.buttonStart.text = "Stop"
//            binding.buttonStop.visibility = View.VISIBLE
//            binding.buttonStop.visibility = View.GONE
//        } else {
//            binding.buttonStart.text = "Start"
//            binding.buttonStart.visibility = View.VISIBLE
//            binding.buttonStop.visibility = View.GONE
//        }
//    }
//
//    private fun startStopTimer() {
//        if(timerStarted) {
//            stopTimer()
//        } else {
//            if(appName.isEmpty()) {
//                Toast.makeText(this, "Pilih aplikasi ", Toast.LENGTH_LONG).show()
//            } else {
//                startTimer()
//            }
//        }
//    }
//
//    private fun checkPrermission(): Boolean {
//        var granted: Boolean
//        var granted2: Boolean
//        var grantedReturn = false
//        val appOps: AppOpsManager = this.getSystemService(APP_OPS_SERVICE) as AppOpsManager
//        val mode = appOps.checkOpNoThrow(
//            AppOpsManager.OPSTR_GET_USAGE_STATS,
//            Process.myUid(), this.packageName
//        )
//        val appOps2: AppOpsManager = this.getSystemService(APP_OPS_SERVICE) as AppOpsManager
//        val lmode2 = appOps2.checkOpNoThrow(
//            AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
//            Process.myUid(), this.packageName
//        )
//            if (mode == AppOpsManager.MODE_DEFAULT && lmode2 == AppOpsManager.MODE_DEFAULT) {
//                granted = (applicationContext.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
//                granted2 = (applicationContext.checkCallingOrSelfPermission(android.Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED)
//            } else {
//                granted = (mode == AppOpsManager.MODE_ALLOWED)
//                granted2 = (lmode2 == AppOpsManager.MODE_ALLOWED)
//            }
//
//        if(granted && granted2) {
//            grantedReturn = true
//        } else {
//            grantedReturn = false
//        }
//
//        return grantedReturn
//    }
//
//    private fun startTimer()
//    {
//        if(appName.isEmpty()) {
//            Toast.makeText(this, "pilih aplikasi", Toast.LENGTH_SHORT).show()
//        } else {
//            SessionManager(this).saveStatusService(true)
//            serviceIntent.putExtra(BackgroundService.TIME_EXTRA, time)
//            serviceIntent.putExtra("appName", appName)
//            startService(serviceIntent)
//            binding.buttonStart.text = "Stop"
//            binding.buttonStart.visibility = View.GONE
//            binding.recycler.visibility = View.GONE
//            binding.buttonStop.visibility = View.VISIBLE
//            timerStarted = SessionManager(this).fetchStatusService()
//        }
//    }
//
//    private fun stopTimer()
//    {
//        SessionManager(this).saveStatusService(false)
//        stopService(serviceIntent)
//        binding.buttonStart.text = "Start"
//        binding.recycler.visibility = View.VISIBLE
//        binding.buttonStart.visibility = View.VISIBLE
//        binding.buttonStop.visibility = View.GONE
//        timerStarted = SessionManager(this).fetchStatusService()
//    }
//
////
////    private val br: BroadcastReceiver = object : BroadcastReceiver() {
////        override fun onReceive(context: Context, intent: Intent) {
////            updateGUI(intent)
////        }
////    }
//
//    override fun onPause() {
//        super.onPause()
////        unregisterReceiver(updateTime)
//        Log.i(TAG, "Unregistered broacast receiver")
//    }
//
//    override fun onStop() {
//        try {
////            unregisterReceiver(updateTime)
//        } catch (e: Exception) {
//            // Receiver was probably already stopped in onPause()
//        }
//        super.onStop()
//    }
//
//    override fun onDestroy() {
//        Log.i(TAG, "Stopped service")
//        binding.buttonStart.text = "Start"
////        SessionManager(this).saveStatusService(false)
////        timerStarted = SessionManager(this).fetchStatusService()
//        super.onDestroy()
//    }
//
//    override fun onResume() {
//        super.onResume()
////        registerReceiver(updateTime, IntentFilter(BackgroundService.TIMER_SERVICE))
//        Log.i(TAG, "Registered broacast receiver")
//    }
//
//    fun getData() {
//        val app = getAllPackages()
//
//        appItem.clear()
//        appItem.addAll(app.map {
//            val itemDetail = DetailItem(
//                this.packageManager.getApplicationLabel(it.packageName),
//                it.packageName
//            )
//            val item = AppItem(
//                this.packageManager.getApplicationIcon(it),
//                itemDetail
////                this.packageManager.getApplicationLabel(it.packageName),
////                it.packageName,
////                this.packageManager.getApplicationIcon(it)
//            )
//            item })
//    }
//
//    private fun getAllPackages():List<ApplicationInfo> {
//        val pm = packageManager
//        return packageManager.queryIntentActivities(
//            Intent(Intent.ACTION_MAIN, null).addCategory(
//                Intent.CATEGORY_LAUNCHER), 0)
//            .filter { it.activityInfo.packageName != this.packageName}
//            .distinctBy { it.activityInfo.packageName }
//            .map { pm.getApplicationInfo(it.activityInfo.packageName, 0) }
//            .sortedBy { pm.getApplicationLabel(it.packageName) }
//    }
//
//    fun PackageManager.getApplicationLabel(packageName: String): String {
//        val info = getApplicationInfo(packageName, 0)
//        return getApplicationLabel(info).toString()
//    }
//
//    fun showRecyclerList() {
//        Log.d(TAG, "percobaan $percobaan")
//        val listAppAdapter = AppListAdapter(appItem)
//
//        binding.recycler.layoutManager = LinearLayoutManager(this)
//        binding.recycler.adapter = listAppAdapter
//        appName.clear()
//
//        listAppAdapter.setOnSwitchCheckedCallback(object: AppListAdapter.OnSwitchCheckedCallback {
//            override fun onSwitchChecked(status: Boolean, data: DetailItem) {
//                if(status) {
//                    appName.add(data)
//                    Toast.makeText(applicationContext, "added ${data.name}", Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, "$appName")
//                } else {
//                    appName.remove(data)
//                    Toast.makeText(applicationContext, "remove ${data.name}", Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, "$appName")
//                }
//            }
//
//        })
//    }
//}

class FocusActivity : AppCompatActivity() {
    var appItem = ArrayList<AppItem>()
    var appName = ArrayList<DetailItem>()
    lateinit var recyler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus)

        title = "Endless Service"

        startActivity(
            Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse(
                    "package:$packageName"
                )
            )
        )
        recyler = findViewById(R.id.recycler)

        getData()
        showRecyclerList()

        findViewById<Button>(R.id.btnStartService).let {
            it.setOnClickListener {
                if(checkPrermission()) {
                    log("START THE FOREGROUND SERVICE ON DEMAND")
                    actionOnService(Actions.START)
                } else {
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
                }
            }
        }

        findViewById<Button>(R.id.btnStopService).let {
            it.setOnClickListener {
                log("STOP THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.STOP)
            }
        }
    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, BackgroundService::class.java).also {
            it.action = action.name
            it.putExtra("appName", appName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
                return
            }
            startService(it)
        }
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
        val listAppAdapter = AppListAdapter(appItem)

        recyler.layoutManager = LinearLayoutManager(this)
        recyler.adapter = listAppAdapter
        appName.clear()

        listAppAdapter.setOnSwitchCheckedCallback(object: AppListAdapter.OnSwitchCheckedCallback {
            override fun onSwitchChecked(status: Boolean, data: DetailItem) {
                if(status) {
                    appName.add(data)
                    Toast.makeText(applicationContext, "added ${data.name}", Toast.LENGTH_SHORT).show()
                } else {
                    appName.remove(data)
                    Toast.makeText(applicationContext, "remove ${data.name}", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

        private fun checkPrermission(): Boolean {
        var granted: Boolean
        var granted2: Boolean
        var grantedReturn = false
        val appOps: AppOpsManager = this.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), this.packageName
        )
        val appOps2: AppOpsManager = this.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val lmode2 = appOps2.checkOpNoThrow(
            AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
            Process.myUid(), this.packageName
        )
            if (mode == AppOpsManager.MODE_DEFAULT && lmode2 == AppOpsManager.MODE_DEFAULT) {
                granted = (applicationContext.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
                granted2 = (applicationContext.checkCallingOrSelfPermission(android.Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED)
            } else {
                granted = (mode == AppOpsManager.MODE_ALLOWED)
                granted2 = (lmode2 == AppOpsManager.MODE_ALLOWED)
            }

        if(granted && granted2) {
            grantedReturn = true
        } else {
            grantedReturn = false
        }

        return grantedReturn
    }
}