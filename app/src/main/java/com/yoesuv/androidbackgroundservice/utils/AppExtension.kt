package com.yoesuv.androidbackgroundservice.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.yoesuv.androidbackgroundservice.BuildConfig

fun logDebug(message: String?) {
    if (BuildConfig.DEBUG) {
        Log.d("result_debug", "$message")
    }
}

fun Int?.addZero(): String {
    if (this == null) {
        return "00"
    } else {
        if (this < 10) {
            return "0$this"
        } else {
            return "$this"
        }
    }
}

fun isTiramisu(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}

fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}