package com.yoesuv.androidbackgroundservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yoesuv.androidbackgroundservice.data.PERM_NOTIFICATION
import com.yoesuv.androidbackgroundservice.databinding.ActivityAlarmBinding
import com.yoesuv.androidbackgroundservice.prefs.PrefAlarm
import com.yoesuv.androidbackgroundservice.utils.AlarmHelper
import com.yoesuv.androidbackgroundservice.utils.AutoStartHelper
import com.yoesuv.androidbackgroundservice.utils.PermissionHelper
import com.yoesuv.androidbackgroundservice.utils.addZero
import com.yoesuv.androidbackgroundservice.utils.checkPermission
import com.yoesuv.androidbackgroundservice.utils.isTiramisu
import java.util.Calendar
import java.util.Locale

class AlarmActivity : AppCompatActivity() {
    companion object {
        const val TIME_PICKER_TAG = "time_picker_tag"

        fun getInstance(context: Context): Intent = Intent(context, AlarmActivity::class.java)
    }

    private lateinit var binding: ActivityAlarmBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pendingTime?.let { setupAlarm(it.first, it.second) }
            }
        }

    private var pendingTime: Pair<Int, Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupButton()
        setupReminderButtons()
        showDataAlarm()
    }

    override fun onResume() {
        super.onResume()
        updateReminderCards()
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

    private fun setupReminderButtons() {
        binding.btnAutoStartSettings.setOnClickListener {
            AutoStartHelper.openAutoStartSettings(this)
        }
        binding.btnBatterySettings.setOnClickListener {
            PermissionHelper.requestIgnoreBatteryOptimizations(this)
        }
    }

    /**
     * Toggle the two reminder cards based on the current device/OEM state.
     * Called from [onResume] so the cards update after the user returns from
     * the OEM settings screen.
     */
    private fun updateReminderCards() {
        // Auto-start card: only meaningful on OEMs that ship an autostart
        // manager (Vivo, Oppo, Xiaomi, Huawei, ...). Stock Android has none.
        val showAutoStart = AutoStartHelper.isAutoStartSupported()
        binding.cardAutoStartPermission.visibility =
            if (showAutoStart) View.VISIBLE else View.GONE

        // Battery-optimization card: visible until the user grants the
        // "ignore battery optimizations" exemption.
        val showBattery = !PermissionHelper.isIgnoringBatteryOptimizations(this)
        binding.cardBatteryOptimization.visibility =
            if (showBattery) View.VISIBLE else View.GONE

        // Hide the containing LinearLayout when there are no cards to show,
        // so it doesn't keep any bottom spacing.
        binding.layoutReminders.visibility =
            if (showAutoStart || showBattery) View.VISIBLE else View.GONE
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val picker =
            MaterialTimePicker
                .Builder()
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
            if (isTiramisu() && !checkPermission(PERM_NOTIFICATION)) {
                // Defer scheduling until the user grants POST_NOTIFICATIONS,
                // otherwise the later notify() call will silently do nothing.
                pendingTime = newHour to newMinute
                requestPermissionLauncher.launch(PERM_NOTIFICATION)
            } else {
                setupAlarm(newHour, newMinute)
            }
        }
    }

    private fun showDataAlarm() {
        binding.tvAlarmTimeHour.text = PrefAlarm.getHour().addZero()
        binding.tvAlarmTimeMinute.text = PrefAlarm.getMinute().addZero()
    }

    private fun setupAlarm(
        hour: Int,
        minute: Int,
    ) {
        AlarmHelper.scheduleAlarm(this, hour, minute)

        PrefAlarm.setHour(hour)
        PrefAlarm.setMinute(minute)
        PrefAlarm.setAlarmSet(true)
        showDataAlarm()
    }
}
