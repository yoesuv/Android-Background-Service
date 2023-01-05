package com.yoesuv.androidbackgroundservice.prefs

import android.content.Context
import android.content.SharedPreferences
import android.content.ContextWrapper
import android.text.TextUtils
import kotlin.RuntimeException

object AppPrefHelper {

    private const val DEFAULT_SUFFIX = "_preferences"
    private lateinit var mPrefs: SharedPreferences

    private fun initPrefs(context: Context, prefsName: String) {
        mPrefs = context.getSharedPreferences(prefsName, ContextWrapper.MODE_PRIVATE)
    }

    // ===== begin preferences int =====
    fun getInt(key: String?, defValue: Int): Int {
        return mPrefs.getInt(key, defValue)
    }

    fun putInt(key: String?, value: Int) {
        val editor = mPrefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }
    // ===== end preferences int =====

    fun remove(key: String) {
        val editor = mPrefs.edit()
        editor.remove(key)
        editor.apply()
    }

    class Builder {
        private var mKey: String? = null
        private var mContext: Context? = null
        private var mUseDefault = false

        fun setPrefsName(prefsName: String): Builder {
            mKey = prefsName
            return this
        }

        fun setContext(context: Context): Builder {
            mContext = context
            return this
        }

        fun setUseDefaultSharedPreference(defaultSharedPreference: Boolean): Builder {
            mUseDefault = defaultSharedPreference
            return this
        }

        fun build() {
            if (mContext == null) {
                throw RuntimeException("Context not set, please set context before building the Prefs instance.")
            }
            if (TextUtils.isEmpty(mKey)) {
                mKey = mContext?.packageName
            }
            if (mUseDefault) {
                mKey += DEFAULT_SUFFIX
            }

            initPrefs(mContext!!, mKey!!)
        }
    }
}