package com.yoesuv.androidbackgroundservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yoesuv.androidbackgroundservice.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notificationWorker = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
        .build()
    private val listWorkState = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton()
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(JOB_NOTIFICATION).observe(this, { workInfo ->
            listWorkState.clear()
            if (workInfo.isNotEmpty()) {
                workInfo?.forEach {
                    listWorkState.add(it.state.name)
                }
            } else {
                listWorkState.add("-")
            }
            binding.tvWorkStatus.text = listWorkState.toString()
        })
    }

    private fun setupButton() {
        binding.btnStart.setOnClickListener {
            startNotificationJob()
        }
        binding.btnStop.setOnClickListener {
            stopNotificationJob()
        }
    }

    private fun startNotificationJob() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            JOB_NOTIFICATION,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorker)
    }

    private fun stopNotificationJob() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.dialog_message)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            WorkManager.getInstance(this@MainActivity).cancelUniqueWork(JOB_NOTIFICATION)
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create()
        builder.show()
    }
}