package com.iuturakulov.openweatherapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iuturakulov.openweatherapp.data.api.ApiHelper
import com.iuturakulov.openweatherapp.data.repository.WeatherRepository
import com.iuturakulov.openweatherapp.ui.main.viewmodel.WeatherViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(WeatherRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}