package com.yoesuv.androidbackgroundservice.prefs

object PrefAlarm {

    private const val KEY_ALARM_HOUR = "KEY_ALARM_HOUR"
    private const val KEY_ALARM_MINUTE = "KEY_ALARM_MINUTE"

    fun setHour(hour: Int) {
        AppPrefHelper.putInt(KEY_ALARM_HOUR, hour)
    }

    fun getHour(): Int {
        return AppPrefHelper.getInt(KEY_ALARM_HOUR, 0)
    }

    fun setMinute(minute: Int) {
        AppPrefHelper.putInt(KEY_ALARM_MINUTE, minute)
    }

    fun getMinute(): Int {
        return AppPrefHelper.getInt(KEY_ALARM_MINUTE, 0)
    }

    fun remove() {
        AppPrefHelper.remove(KEY_ALARM_HOUR)
        AppPrefHelper.remove(KEY_ALARM_MINUTE)
    }

}