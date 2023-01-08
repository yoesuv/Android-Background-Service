package com.yoesuv.androidbackgroundservice.data

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.yoesuv.androidbackgroundservice.BuildConfig

const val JOB_NOTIFICATION = "job_notification"
const val CHANNEL_ID = BuildConfig.APPLICATION_ID + "_channel"
const val CHANNEL_ALARM_ID = BuildConfig.APPLICATION_ID + "_alarm_channel"
const val STORE_NAME = "prefs_" + BuildConfig.APPLICATION_ID

// permission
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val PERM_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS