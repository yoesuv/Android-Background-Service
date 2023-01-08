package com.yoesuv.androidbackgroundservice

import android.app.Application
import com.yoesuv.androidbackgroundservice.prefs.PrefsHelper

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // init prefs helper
        PrefsHelper.Builder()
            .setContext(this)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

}