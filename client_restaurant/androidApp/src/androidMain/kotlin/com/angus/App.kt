package com.angus

import android.app.Application
import com.angus.di.firebaseModule
import di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App.applicationContext); modules(
            appModule(),
            firebaseModule
        )
        }
    }
}