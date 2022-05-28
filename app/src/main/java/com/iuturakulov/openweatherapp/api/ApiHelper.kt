package com.iuturakulov.openweatherapp.api

import com.iuturakulov.openweatherapp.model.models.SearchResults
import com.iuturakulov.openweatherapp.model.models.Weather
import retrofit2.Response

interface ApiHelper {
    suspend fun getWeatherData(latitude: Double, longitude: Double,
        apiKey: String) : Response<Weather>

    suspend fun searchWeatherLocationData(
        locationName: String, apiKey: String
    ) : Response<SearchResults>
}