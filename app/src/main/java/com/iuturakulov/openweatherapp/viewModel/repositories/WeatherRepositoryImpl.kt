package com.iuturakulov.openweatherapp.viewModel.repositories

import com.iuturakulov.openweatherapp.api.ApiHelper
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ) = apiHelper.getWeatherData(latitude, longitude, apiKey)

    suspend fun getSearchLocationData(
        locationName: String,
        apiKey: String
    ) = apiHelper.searchWeatherLocationData(locationName, apiKey)
}
