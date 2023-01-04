package com.yoesuv.androidbackgroundservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yoesuv.androidbackgroundservice.data.JOB_NOTIFICATION
import com.yoesuv.androidbackgroundservice.data.PERM_NOTIFICATION
import com.yoesuv.androidbackgroundservice.databinding.ActivityMainBinding
import com.yoesuv.androidbackgroundservice.utils.checkPermission
import com.yoesuv.androidbackgroundservice.utils.isTiramisu
import com.yoesuv.androidbackgroundservice.utils.logDebug
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val notificationWorker = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
        .setInitialDelay(15, TimeUnit.MINUTES)
        .build()
    private val listWorkState = mutableListOf<String>()

    // permission
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        logDebug("MainActivity # request permission is $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton()
        checkPermissionNotification()

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(JOB_NOTIFICATION).observe(this) { workInfo ->
            listWorkState.clear()
            if (workInfo.isNotEmpty()) {
                workInfo?.forEach {
                    listWorkState.add(it.state.name)
                }
            } else {
                listWorkState.add("-")
            }
            binding.tvWorkStatus.text = listWorkState.toString()
        }
    }

    private fun setupButton() {
        binding.btnStart.setOnClickListener {
            startNotificationJob()
        }
        binding.btnStop.setOnClickListener {
            stopNotificationJob()
        }
        binding.btnAlarmManager.setOnClickListener {
            startActivity(AlarmActivity.getInstance(this))
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

    private fun checkPermissionNotification() {
        if (isTiramisu()) {
            val check = checkPermission(PERM_NOTIFICATION)
            logDebug("MainActivity # permission push notification $check")
            if (!check) {
                requestPermissionLauncher.launch(PERM_NOTIFICATION)
            }
        }
    }

}