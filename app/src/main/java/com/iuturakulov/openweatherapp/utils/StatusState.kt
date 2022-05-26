package com.iuturakulov.openweatherapp.utils

import com.iuturakulov.openweatherapp.model.models.WeatherForecast

sealed class StatusState
data class FailureState(val msg: String) : StatusState()
data class SuccessState(val response: WeatherForecast) : StatusState()