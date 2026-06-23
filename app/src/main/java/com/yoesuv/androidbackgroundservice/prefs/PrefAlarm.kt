package com.yoesuv.androidbackgroundservice.prefs

object PrefAlarm {
    private const val KEY_ALARM_HOUR = "KEY_ALARM_HOUR"
    private const val KEY_ALARM_MINUTE = "KEY_ALARM_MINUTE"
    private const val KEY_ALARM_IS_SET = "KEY_ALARM_IS_SET"

    fun setHour(hour: Int) {
        PrefsHelper.putInt(KEY_ALARM_HOUR, hour)
    }

    fun getHour(): Int = PrefsHelper.getInt(KEY_ALARM_HOUR, 0)

    fun setMinute(minute: Int) {
        PrefsHelper.putInt(KEY_ALARM_MINUTE, minute)
    }

    fun getMinute(): Int = PrefsHelper.getInt(KEY_ALARM_MINUTE, 0)

    fun setAlarmSet(isSet: Boolean) {
        PrefsHelper.putBoolean(KEY_ALARM_IS_SET, isSet)
    }

    fun isAlarmSet(): Boolean = PrefsHelper.getBoolean(KEY_ALARM_IS_SET, false)

    fun remove() {
        PrefsHelper.remove(KEY_ALARM_HOUR)
        PrefsHelper.remove(KEY_ALARM_MINUTE)
        PrefsHelper.putBoolean(KEY_ALARM_IS_SET, false)
    }
}
