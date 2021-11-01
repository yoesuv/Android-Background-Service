package com.yoesuv.androidbackgroundservice

fun Int.addZero(): String {
    if (this < 10) {
        return "0$this"
    } else {
        return "$this"
    }
}