package com.breaktime.lab2.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Preferences @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPref =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun getNightMode() = sharedPref.getBoolean("nightMode", false)

    fun setNightMode(isNight: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("nightMode", isNight)
        editor.apply()
    }
}