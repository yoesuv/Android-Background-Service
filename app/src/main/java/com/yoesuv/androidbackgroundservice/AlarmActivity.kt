package com.yoesuv.androidbackgroundservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.yoesuv.androidbackgroundservice.databinding.ActivityAlarmBinding

class AlarmActivity: AppCompatActivity() {

    companion object {
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

        }
    }

}