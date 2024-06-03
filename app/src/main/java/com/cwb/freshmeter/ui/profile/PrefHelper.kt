package com.cwb.freshmeter.ui.profile

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context: Context) {
    private val PREFS_NAME = "FreshMeter.pref"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun putBoolean(key: String, value: Boolean, email: String) {
        editor.putBoolean("$email:$key", value).apply()
    }

    fun getBoolean(key: String, email: String): Boolean {
        return sharedPref.getBoolean("$email:$key", false)
    }

    fun putString(key: String, value: String, email: String) {
        editor.putString("$email:$key", value).apply()
    }

    fun getString(key: String, email: String): String? {
        return sharedPref.getString("$email:$key", null)
    }
}