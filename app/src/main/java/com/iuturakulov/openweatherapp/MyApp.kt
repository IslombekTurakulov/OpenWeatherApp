package com.iuturakulov.openweatherapp

import android.app.Application
import com.iuturakulov.openweatherapp.storage.SharedPreferencesStorage
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application()