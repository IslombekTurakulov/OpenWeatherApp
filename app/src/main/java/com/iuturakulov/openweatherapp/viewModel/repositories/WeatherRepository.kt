package com.iuturakulov.openweatherapp.viewModel.repositories

import com.iuturakulov.openweatherapp.model.models.SearchResults
import com.iuturakulov.openweatherapp.model.models.Weather
import retrofit2.Response

interface WeatherRepository {
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        apiKey: String,
    ): Response<Weather>

    suspend fun getSearchLocationData(
        locationName: String,
        apiKey: String,
    ): Response<SearchResults>
}