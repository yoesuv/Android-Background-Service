package com.yoesuv.androidbackgroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yoesuv.androidbackgroundservice.data.CHANNEL_ID

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val mContext = context

    override fun doWork(): Result {
        Log.d("result_debug", "NotificationWorker # do work notif user")
        val notificationManager: NotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(mContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setContentTitle(mContext.getString(R.string.push_notification_background_service_title))
            .setContentText(mContext.getString(R.string.push_notification_background_service_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val name = mContext.getString(R.string.app_name)
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Notification for background service"
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
        return Result.success()
    }
}