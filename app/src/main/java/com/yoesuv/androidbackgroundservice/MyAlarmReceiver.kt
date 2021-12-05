package com.yoesuv.androidbackgroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yoesuv.androidbackgroundservice.prefs.StoreAlarm
import com.yoesuv.androidbackgroundservice.prefs.appStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
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

        notificationManager.notify(0, notificationBuilder.build())
        runBlocking {
            withContext(Dispatchers.IO) {
                resetAlarm(context)
            }
        }
    }

    private suspend fun resetAlarm(context: Context?) = coroutineScope {
        context?.appStore?.let { store ->
            val storeAlarm = StoreAlarm(store)
            storeAlarm.removeAlarm()
        }
    }

}