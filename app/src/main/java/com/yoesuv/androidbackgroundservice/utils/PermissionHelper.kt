package com.yoesuv.androidbackgroundservice.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings

/**
 * Helper for checking / requesting "Ignore battery optimizations".
 *
 * Doze can defer inexact alarms (and even exact alarms if the system revokes
 * them). Allowing the app to ignore battery optimizations prevents the OEM
 * power manager from killing the scheduled [AlarmManager] broadcast between
 * the time the user schedules it and the moment it should fire.
 */
object PermissionHelper {
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return false
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    /**
     * Opens the system "Ignore battery optimizations" prompt for this app.
     * Requires the `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` permission in the
     * manifest.
     */
    fun requestIgnoreBatteryOptimizations(context: Context) {
        val intent =
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                .setData(Uri.fromParts("package", context.packageName, null))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            // Fallback to the global battery-optimization list
            try {
                context.startActivity(
                    Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            } catch (_: Exception) {
                // Last resort: app details
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", context.packageName, null))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            }
        }
    }
}
