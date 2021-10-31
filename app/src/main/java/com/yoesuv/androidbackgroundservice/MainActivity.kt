package com.yoesuv.androidbackgroundservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yoesuv.androidbackgroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton()
    }

    private fun setupButton() {
        binding.btnStart.setOnClickListener {
            Log.d("result_debug", "MainActivity # START")
        }
        binding.btnStop.setOnClickListener {
            Log.d("result_debug", "MainActivity # STOP")
        }
    }
}