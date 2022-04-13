package com.iuturakulov.openweatherapp.data.repository

import com.iuturakulov.openweatherapp.data.api.ApiHelper
import com.iuturakulov.openweatherapp.data.model.models.WeatherForecast
import io.reactivex.Single

class WeatherRepository(private val apiHelper: ApiHelper) {

    fun getUsers(): Single<List<WeatherForecast>> {
        return apiHelper.getCurrentWeather()
    }
}