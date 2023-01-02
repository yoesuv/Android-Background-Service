package com.yoesuv.androidbackgroundservice.prefs

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.yoesuv.androidbackgroundservice.data.STORE_NAME

val Context.appStore by preferencesDataStore( name = STORE_NAME)