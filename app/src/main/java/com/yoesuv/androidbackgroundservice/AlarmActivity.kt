package com.yoesuv.androidbackgroundservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yoesuv.androidbackgroundservice.databinding.ActivityAlarmBinding
import com.yoesuv.androidbackgroundservice.prefs.PrefAlarm
import com.yoesuv.androidbackgroundservice.utils.addZero
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setupToolbar()
        setupButton()
        showDataAlarm()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
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
        val calendar = Calendar.getInstance(Locale.getDefault())
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText(R.string.button_set_alarm_time)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()
        picker.show(supportFragmentManager, TIME_PICKER_TAG)
        picker.addOnPositiveButtonClickListener {
            val newHour = picker.hour
            val newMinute = picker.minute
            setupAlarm(newHour, newMinute)
        }
    }

    private fun showDataAlarm() {
        binding.tvAlarmTimeHour.text = PrefAlarm.getHour().addZero()
        binding.tvAlarmTimeMinute.text = PrefAlarm.getMinute().addZero()
    }

    private fun setupAlarm(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        var flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        }

        val intent = Intent(this, MyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flags)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        PrefAlarm.setHour(hour)
        PrefAlarm.setMinute(minute)
        showDataAlarm()
    }

}