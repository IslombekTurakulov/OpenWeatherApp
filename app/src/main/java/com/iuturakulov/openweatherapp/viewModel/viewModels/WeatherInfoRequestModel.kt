package com.iuturakulov.openweatherapp.viewModel.viewModels

import com.iuturakulov.openweatherapp.utils.StatusState

interface WeatherInfoRequestModel {
    suspend fun getWeatherInfo(
        lat: Double, lon: Double
    ): StatusState
}