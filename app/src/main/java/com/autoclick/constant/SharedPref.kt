package com.autoclick.constant

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object SharedPref {
    var mSharedPref: SharedPreferences? = null
    const val NAME_PREF = "ACCESIBILITY_REF"
    const val ACCESSIBILITY_PERMISSION = "accessibilityPermission"

    fun init(context: Context) {
        if (mSharedPref == null) mSharedPref =
            context.getSharedPreferences(NAME_PREF, Activity.MODE_PRIVATE)
    }

    fun read(key: String?, defValue: String?): String? {
        return mSharedPref!!.getString(key, defValue)
    }

    fun write(key: String?, value: String?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun read(key: String?, defValue: Boolean): Boolean {
        return mSharedPref!!.getBoolean(key, defValue)
    }

    fun write(key: String?, value: Boolean) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun read(key: String?, defValue: Int): Int {
        return mSharedPref!!.getInt(key, defValue)
    }

    fun write(key: String?, value: Int?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putInt(key, value!!).apply()
    }
}