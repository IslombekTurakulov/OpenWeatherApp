package com.iuturakulov.openweatherapp.viewModel.repositories

import com.iuturakulov.openweatherapp.api.ApiHelper
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getWeatherData(latitude: Double, longitude: Double, apiKey: String, unit: String) =
        apiHelper.getWeatherData(latitude, longitude, apiKey, unit)

    suspend fun getSearchLocationData(locationName: String, apiKey: String, unit: String)  =
        apiHelper.searchWeatherLocationData(locationName, apiKey, unit)
}