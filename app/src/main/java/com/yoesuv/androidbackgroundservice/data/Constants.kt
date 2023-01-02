package com.yoesuv.androidbackgroundservice.data

import com.yoesuv.androidbackgroundservice.BuildConfig

const val JOB_NOTIFICATION = "job_notification"
const val CHANNEL_ID = BuildConfig.APPLICATION_ID + "_channel"
const val CHANNEL_ALARM_ID = BuildConfig.APPLICATION_ID + "_alarm_channel"
const val STORE_NAME = "prefs_" + BuildConfig.APPLICATION_ID