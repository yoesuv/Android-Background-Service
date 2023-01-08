package com.yoesuv.androidbackgroundservice.prefs

object PrefAlarm {

    private const val KEY_ALARM_HOUR = "KEY_ALARM_HOUR"
    private const val KEY_ALARM_MINUTE = "KEY_ALARM_MINUTE"

    fun setHour(hour: Int) {
        PrefsHelper.putInt(KEY_ALARM_HOUR, hour)
    }

    fun getHour(): Int {
        return PrefsHelper.getInt(KEY_ALARM_HOUR, 0)
    }

    fun setMinute(minute: Int) {
        PrefsHelper.putInt(KEY_ALARM_MINUTE, minute)
    }

    fun getMinute(): Int {
        return PrefsHelper.getInt(KEY_ALARM_MINUTE, 0)
    }

    fun remove() {
        PrefsHelper.remove(KEY_ALARM_HOUR)
        PrefsHelper.remove(KEY_ALARM_MINUTE)
    }

}