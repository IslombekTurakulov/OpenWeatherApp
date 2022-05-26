package com.iuturakulov.openweatherapp.viewModel.viewModels

import com.iuturakulov.openweatherapp.utils.StatusState

interface WeatherInfoShowModel {
    suspend fun getWeatherInfo(cityName: String) : StatusState
}