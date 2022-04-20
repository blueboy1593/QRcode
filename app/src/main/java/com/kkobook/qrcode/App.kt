package com.kkobook.qrcode

import android.app.Application
import com.kkobook.qrcode.utils.QRSharedPreferences

class App : Application() {

    companion object {
        lateinit var prefs : QRSharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        prefs = QRSharedPreferences(applicationContext)
    }
}