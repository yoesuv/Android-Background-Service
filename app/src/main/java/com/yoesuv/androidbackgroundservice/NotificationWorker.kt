package com.yoesuv.androidbackgroundservice

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("result_debug","NotificationWorker # show notification")
        return Result.success()
    }
}