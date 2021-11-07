package com.yoesuv.androidbackgroundservice

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