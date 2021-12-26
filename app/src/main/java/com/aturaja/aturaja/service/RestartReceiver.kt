package com.aturaja.aturaja.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RestartReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.startService(Intent(p0.applicationContext, BackgroundService::class.java))
    }
}