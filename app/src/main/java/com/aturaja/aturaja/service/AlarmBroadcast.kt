package com.aturaja.aturaja.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.aturaja.aturaja.R
import com.aturaja.aturaja.activity.HomeActivity

class AlarmBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val bundle: Bundle? = p1?.extras
        val text: String? = bundle?.getString("title")
        var sentence = setString(bundle?.getString("startTime"),  bundle?.getString("endTime"))

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context,HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var mBuilder = NotificationCompat.Builder(context, "notify_001")
            .setSmallIcon(R.mipmap.ic_launcher_logo)
            .setContentTitle("$text")
            .setContentText("$sentence")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channel =
                NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        val notification = mBuilder.build()
        notificationManager.notify(1, notification)
    }

    private fun setString(string: String?, string1: String?): String {
        var sentence = ""
        if(string1 == null) {
            sentence = "$string"
        } else {
            sentence = "$string - $string1"
        }

        return sentence
    }
}