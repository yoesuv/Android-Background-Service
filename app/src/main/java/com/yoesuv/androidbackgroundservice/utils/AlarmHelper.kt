package com.yoesuv.androidbackgroundservice.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yoesuv.androidbackgroundservice.MyAlarmReceiver
import com.yoesuv.androidbackgroundservice.data.ACTION_ALARM_FIRED
import java.util.Calendar

object AlarmHelper {
    fun scheduleAlarm(
        context: Context,
        hour: Int,
        minute: Int,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // If the chosen time already passed today, schedule it for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        var flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags = flags or PendingIntent.FLAG_IMMUTABLE
        }

        val intent =
            Intent(context, MyAlarmReceiver::class.java).apply {
                action = ACTION_ALARM_FIRED
            }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags)

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
