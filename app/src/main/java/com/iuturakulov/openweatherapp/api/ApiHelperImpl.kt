package com.iuturakulov.openweatherapp.api

import com.iuturakulov.openweatherapp.model.models.SearchResults
import com.iuturakulov.openweatherapp.model.models.WeatherForecast
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        unit: String
    ) = apiService.getWeatherData(latitude, longitude, apiKey, unit)

    override suspend fun searchWeatherLocationData(
        locationName: String,
        apiKey: String,
        unit: String
    ): Response<SearchResults> =
        apiService.searchWeatherLocationData(locationName, apiKey, unit)

    override suspend fun callForCityWeather(
        locationName: String,
        apiKey: String
    ): WeatherForecast =
        apiService.callForCityWeather(locationName, apiKey)
}