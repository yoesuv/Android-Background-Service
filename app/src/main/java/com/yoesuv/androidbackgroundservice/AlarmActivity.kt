package com.yoesuv.androidbackgroundservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.tvAlarmTime.text = "${newHour.addZero()}:${newMinute.addZero()}"
        }
    }

}