package com.iuturakulov.openweatherapp.utils

import com.iuturakulov.openweatherapp.model.models.HourlyDaily
import com.iuturakulov.openweatherapp.model.models.Weather

sealed class StatusState

data class FailureState(val msg: String) : StatusState()

data class SuccessState(val response: HourlyDaily) : StatusState()