package com.yoesuv.androidbackgroundservice

import android.app.Application
import com.yoesuv.androidbackgroundservice.prefs.AppPrefHelper

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // init prefs helper
        AppPrefHelper.Builder()
            .setContext(this)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

}