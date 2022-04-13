package com.iuturakulov.openweatherapp.data.api

import com.iuturakulov.openweatherapp.data.model.models.WeatherForecast
import io.reactivex.Single

interface ApiService {
    fun getCurrentWeather() : Single<List<WeatherForecast>>
}
