package com.iuturakulov.openweatherapp.api

import android.app.appsearch.SearchResults
import com.iuturakulov.openweatherapp.model.models.Weather
import com.iuturakulov.openweatherapp.model.models.WeatherForecast
import retrofit2.Response

interface ApiHelper {
    suspend fun getWeatherData(
        latitude: Double, longitude: Double,
        apiKey: String, unit: String
    ): Response<Weather>

    suspend fun searchWeatherLocationData(
        locationName: String, apiKey: String, unit: String
    ): Response<SearchResults>

    suspend fun callForCityWeather(
        locationName: String,
        apiKey: String
    ): WeatherForecast
}