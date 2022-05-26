package com.iuturakulov.openweatherapp

import android.content.Context
import com.iuturakulov.openweatherapp.view.activities.MainActivity
import com.iuturakulov.openweatherapp.view.activities.SplashActivity

import dagger.BindsInstance
import dagger.Component

import javax.inject.Singleton

@Singleton
@Component
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(activity: MainActivity)
    fun inject(activity: SplashActivity)
}