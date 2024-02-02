package com.yoesuv.androidbackgroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yoesuv.androidbackgroundservice.data.CHANNEL_ALARM_ID
import com.yoesuv.androidbackgroundservice.prefs.PrefAlarm

class MyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_DATE_CHANGED -> {
                // to do when date changed
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                // to do when boot
            }
            else -> {
                setupNotification(context)
            }
        }
    }

    private fun setupNotification(context: Context?) {
        val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ALARM_ID)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setContentTitle(context.getString(R.string.button_alarm_manager))
            .setContentText(context.getString(R.string.push_notification_alarm_manager))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val channel = NotificationChannel(CHANNEL_ALARM_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Notification for alarm manager"
            notificationManager.createNotificationChannel(channel)
        }

        if (PrefAlarm.getHour() != 0) {
            notificationManager.notify(0, notificationBuilder.build())
        }
        PrefAlarm.remove()
    }

}