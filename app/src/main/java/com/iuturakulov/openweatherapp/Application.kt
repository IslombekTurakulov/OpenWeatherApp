package com.iuturakulov.openweatherapp

import android.app.Application
import com.iuturakulov.openweatherapp.viewModel.viewModels.WeatherInfoShowModelImpl
import com.iuturakulov.openweatherapp.storage.SharedPreferencesStorage
import timber.log.Timber

class App : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    val preferenceStorage by lazy {
        SharedPreferencesStorage(this)
    }
    val weatherModel by lazy {
        WeatherInfoShowModelImpl()
    }

}