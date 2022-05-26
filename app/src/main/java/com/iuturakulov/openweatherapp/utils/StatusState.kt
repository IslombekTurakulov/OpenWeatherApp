package com.iuturakulov.openweatherapp.utils

import com.iuturakulov.openweatherapp.model.models.SearchResults
import com.iuturakulov.openweatherapp.model.models.WeatherForecast
import retrofit2.Response

sealed class StatusState
data class FailureState(val msg: String) : StatusState()
data class SuccessState(val response: WeatherForecast) : StatusState()

data class SuccessResultState(val response: Response<SearchResults>) : StatusState()