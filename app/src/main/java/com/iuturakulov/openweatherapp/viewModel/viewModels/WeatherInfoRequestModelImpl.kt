package com.iuturakulov.openweatherapp.viewModel.viewModels

import com.iuturakulov.openweatherapp.BuildConfig
import com.iuturakulov.openweatherapp.api.ApiService
import com.iuturakulov.openweatherapp.api.RetroFitInstance
import com.iuturakulov.openweatherapp.utils.FailureState
import com.iuturakulov.openweatherapp.utils.StatusState
import com.iuturakulov.openweatherapp.utils.SuccessState
import javax.inject.Inject

class WeatherInfoRequestModelImpl @Inject constructor() : WeatherInfoRequestModel {
    override suspend fun getWeatherInfo(
        lat: Double, lon: Double
    ): StatusState {
        val apiInterface: ApiService = RetroFitInstance.client.create(ApiService::class.java)
        return try {
            SuccessState(apiInterface.getWeatherData(lat, lon, BuildConfig.APP_ID, "hourly, daily"))
        } catch (cause: Throwable) {
            FailureState(msg = "Network call failure ${cause.localizedMessage}")
        }
    }
}