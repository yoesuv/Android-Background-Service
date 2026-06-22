package com.yoesuv.androidbackgroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yoesuv.androidbackgroundservice.data.ACTION_ALARM_FIRED
import com.yoesuv.androidbackgroundservice.data.CHANNEL_ALARM_ID
import com.yoesuv.androidbackgroundservice.prefs.PrefAlarm
import com.yoesuv.androidbackgroundservice.utils.AlarmHelper

class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        when (intent?.action) {
            Intent.ACTION_DATE_CHANGED -> {
                // to do when date changed
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                val ctx = context ?: return
                val hour = PrefAlarm.getHour()
                val minute = PrefAlarm.getMinute()

                // Reschedule only if an alarm was previously set
                if (PrefAlarm.isAlarmSet()) {
                    AlarmHelper.scheduleAlarm(ctx, hour, minute)
                }
            }

            ACTION_ALARM_FIRED -> {
                setupNotification(context)
            }

            else -> {
                // Ignore unknown intents
            }
        }
    }

    private fun setupNotification(context: Context?) {
        val ctx = context ?: return
        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        val notificationBuilder =
            NotificationCompat
                .Builder(ctx, CHANNEL_ALARM_ID)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setContentTitle(ctx.getString(R.string.button_alarm_manager))
                .setContentText(ctx.getString(R.string.push_notification_alarm_manager))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = ctx.getString(R.string.app_name)
            val channel = NotificationChannel(CHANNEL_ALARM_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Notification for alarm manager"
            notificationManager.createNotificationChannel(channel)
        }

        if (PrefAlarm.isAlarmSet()) {
            notificationManager.notify(0, notificationBuilder.build())
        }
        PrefAlarm.remove()
    }
}
