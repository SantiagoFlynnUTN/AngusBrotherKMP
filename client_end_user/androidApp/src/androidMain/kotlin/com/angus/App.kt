package com.angus

import android.app.Application
import com.angus.di.firebaseModule
import com.angus.di.locationDataSourceModule
import com.angus.di.locationServiceModule
import com.angus.di.locationTrackerModule
import di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App.applicationContext).modules(
                appModule(),
                locationTrackerModule,
                locationServiceModule,
                locationDataSourceModule,
                firebaseModule
            )
        }
    }
}
