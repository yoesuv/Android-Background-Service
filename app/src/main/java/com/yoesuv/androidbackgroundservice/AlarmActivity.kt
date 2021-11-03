package com.yoesuv.androidbackgroundservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yoesuv.androidbackgroundservice.databinding.ActivityAlarmBinding
import java.util.*

class AlarmActivity: AppCompatActivity() {

    companion object {
        const val TIME_PICKER_TAG = "time_picker_tag"
        fun getInstance(context: Context): Intent {
            return Intent(context, AlarmActivity::class.java)
        }
    }

    private lateinit var binding: ActivityAlarmBinding

    private lateinit var alarmManager: AlarmManager
    val dataStore: DataStore<Preferences> by preferencesDataStore( name = STORE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setupToolbar()
        setupButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        supportActionBar?.setTitle(R.string.button_alarm_manager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupButton() {
        binding.btnSetAlarmTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(calendar.get(Calendar.HOUR))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText(R.string.button_set_alarm_time)
            .build()
        picker.show(supportFragmentManager, TIME_PICKER_TAG)
        picker.addOnPositiveButtonClickListener {
            val newHour = picker.hour
            val newMinute = picker.minute
            setupAlarm(newHour, newMinute)
            binding.tvAlarmTime.text = "${newHour.addZero()}:${newMinute.addZero()}"
        }
    }

    private fun setupAlarm(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val intent = Intent(this, MyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

}