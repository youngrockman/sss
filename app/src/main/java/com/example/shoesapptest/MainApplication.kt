package com.example.shoesapptest

import android.app.Application
import com.example.shoesapptest.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()



        startKoin {
            androidContext(this@MainApplication)
            modules(appModules)
        }
    }
}
