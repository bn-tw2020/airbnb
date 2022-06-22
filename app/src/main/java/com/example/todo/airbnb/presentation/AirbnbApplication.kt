package com.example.todo.airbnb.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AirbnbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}