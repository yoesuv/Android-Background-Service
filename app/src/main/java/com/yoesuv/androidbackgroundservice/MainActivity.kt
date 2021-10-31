package com.yoesuv.androidbackgroundservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yoesuv.androidbackgroundservice.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notificationWorker = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton()
    }

    private fun setupButton() {
        binding.btnStart.setOnClickListener {
            startNotificationJob()
        }
        binding.btnStop.setOnClickListener {
            Log.d("result_debug", "MainActivity # STOP")
        }
    }

    private fun startNotificationJob() {
        Log.d("result_debug", "MainActivity # start notification job")
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(JOB_NOTIFICATION).observe(this, { workInfo ->
            Log.d("result_debug", "MainActivity # $workInfo")
        })
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            JOB_NOTIFICATION,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorker)
    }

    private fun stopNotificationJob() {
        WorkManager.getInstance(this).cancelUniqueWork(JOB_NOTIFICATION)
    }
}