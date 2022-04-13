package com.iuturakulov.openweatherapp

import android.app.Application
import timber.log.Timber


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
