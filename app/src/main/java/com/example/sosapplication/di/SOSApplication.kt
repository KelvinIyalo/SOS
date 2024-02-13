package com.example.sosapplication.di

import android.app.Application
import com.example.sosapplication.utils.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SOSApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        if (Utility.hasPermission(this)) {
            Utility.requestLocation(this)
        }
    }
}