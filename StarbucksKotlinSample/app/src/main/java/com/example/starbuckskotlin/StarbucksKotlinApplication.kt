package com.example.starbuckskotlin

import android.app.Application
import com.example.starbuckskotlin.api.SessionManager

class StarbucksKotlinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.getInstance().init(this)
    }
}