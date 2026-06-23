package com.yoesuv.androidbackgroundservice.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.util.Locale

/**
 * Helper for opening the OEM-specific "AutoStart" / "Background launch" screen.
 *
 * Chinese OEMs (Vivo, Oppo, Xiaomi, Huawei, Honor, OnePlus, Asus, Samsung)
 * install a system permission manager that silently blocks broadcasts and
 * services from apps the user hasn't explicitly allow-listed. There is no
 * public API to query or toggle this state, so the best we can do is route
 * the user to the right settings activity.
 *
 * On stock Android / Pixel, there is nothing to open here so this helper
 * reports [isAutoStartSupported] = false and callers can hide the reminder.
 */
object AutoStartHelper {
    /**
     * Whether the current device is likely to ship an AutoStart / Background
     * launch permission screen that we should send the user to.
     */
    fun isAutoStartSupported(): Boolean {
        val manufacturer = Build.MANUFACTURER?.lowercase(Locale.getDefault()) ?: return false
        return manufacturer in SUPPORTED_MANUFACTURERS
    }

    /**
     * Try every known OEM intent. The first one that resolves is launched.
     * Returns `true` if an activity was actually started, `false` otherwise.
     */
    fun openAutoStartSettings(context: Context): Boolean {
        for (intent in buildIntents(context.packageName)) {
            if (tryStart(context, intent)) return true
        }
        // Last resort: app details page so the user can at least find the
        // OEM-specific toggle manually.
        return tryStart(
            context,
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", context.packageName, null))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }

    private fun tryStart(
        context: Context,
        intent: Intent,
    ): Boolean =
        try {
            if (context.packageManager.resolveActivity(intent, 0) != null) {
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }

    private fun buildIntents(packageName: String): List<Intent> =
        listOf(
            // Vivo / iQOO / OriginOS / FuntouchOS
            Intent().setComponent(
                ComponentName(
                    "com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity",
                ),
            ),
            Intent().setComponent(
                ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity",
                ),
            ),
            // Oppo / ColorOS / RealmeUI
            Intent().setComponent(
                ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity",
                ),
            ),
            Intent().setComponent(
                ComponentName(
                    "com.oppo.safe",
                    "com.oppo.safe.permission.startup.StartupAppListActivity",
                ),
            ),
            // OnePlus (shares Oppo shells on newer ColorOS, but keep its own too)
            Intent().setComponent(
                ComponentName(
                    "com.oneplus.security",
                    "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity",
                ),
            ),
            // Xiaomi / MIUI / HyperOS
            Intent().setComponent(
                ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity",
                ),
            ),
            // Huawei / EMUI
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity",
                ),
            ),
            // Honor / MagicOS
            Intent().setComponent(
                ComponentName(
                    "com.hihonor.systemmanager",
                    "com.hihonor.systemmanager.startupmgr.ui.StartupNormalAppListActivity",
                ),
            ),
            // Asus / ZenUI
            Intent().setComponent(
                ComponentName(
                    "com.asus.mobilemanager",
                    "com.asus.mobilemanager.entry.FunctionActivity",
                ),
            ),
            // Samsung (Device Maintenance → Battery → Auto-launch / Background)
            Intent().setComponent(
                ComponentName(
                    "com.samsung.android.lool",
                    "com.samsung.android.sm.battery.ui.BatteryActivity",
                ),
            ),
            // Generic AOSP fallback (Battery optimization)
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                .setData(Uri.fromParts("package", packageName, null)),
        ).map { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

    private val SUPPORTED_MANUFACTURERS =
        setOf(
            "vivo",
            "iqoo",
            "oppo",
            "realme",
            "oneplus",
            "xiaomi",
            "redmi",
            "huawei",
            "honor",
            "asus",
            "samsung",
        )
}
