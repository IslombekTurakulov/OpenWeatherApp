package com.iuturakulov.openweatherapp.api

import com.iuturakulov.openweatherapp.model.models.SearchResults
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper {
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ) = apiService.getWeatherData(latitude, longitude, apiKey)

    override suspend fun searchWeatherLocationData(locationName: String, apiKey: String): Response<SearchResults> =
        apiService.searchWeatherLocationData(locationName, apiKey)
}