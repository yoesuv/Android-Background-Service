package com.yoesuv.androidbackgroundservice.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreAlarm(private val dataStore: DataStore<Preferences>) {

    companion object {
        val KEY_ALARM_HOUR = intPreferencesKey("KEY_ALARM_HOUR")
        val KEY_ALARM_MINUTE = intPreferencesKey("KEY_ALARM_MINUTE")
    }

    val alarmHour: Flow<Int?> = dataStore.data.map {
        it[KEY_ALARM_HOUR]
    }

    val alarmMinute: Flow<Int?> = dataStore.data.map {
        it[KEY_ALARM_MINUTE]
    }

    suspend fun setAlarm(hour: Int, minute: Int) {
        dataStore.edit {
            it[KEY_ALARM_HOUR] = hour
            it[KEY_ALARM_MINUTE] = minute
        }
    }

    suspend fun removeAlarm() {
        dataStore.edit {
            it.remove(KEY_ALARM_HOUR)
            it.remove(KEY_ALARM_MINUTE)
        }
    }

}