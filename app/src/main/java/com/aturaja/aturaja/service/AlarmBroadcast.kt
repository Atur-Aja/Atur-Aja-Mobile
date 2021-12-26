package com.aturaja.aturaja.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aturaja.aturaja.R
import com.aturaja.aturaja.activity.HomeActivity

class AlarmBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context,HomeActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var mBuilder = NotificationCompat.Builder(context, "notify_001")
            .setSmallIcon(R.drawable.icon_people_addschedule)
            .setContentTitle("My Alarm")
            .setContentText("Hello World!\nmalu-malu tapi mau")
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
}