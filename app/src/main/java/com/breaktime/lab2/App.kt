package com.breaktime.lab2

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.breaktime.lab2.util.Preferences
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var preferences: Preferences

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
        val mode =
            if (preferences.getNightMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}