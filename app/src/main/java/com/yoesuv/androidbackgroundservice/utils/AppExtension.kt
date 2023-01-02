package com.yoesuv.androidbackgroundservice.utils

import android.os.Build

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