package com.iuturakulov.openweatherapp.viewModel.viewModels

import com.iuturakulov.openweatherapp.BuildConfig
import com.iuturakulov.openweatherapp.api.ApiHelperImpl
import com.iuturakulov.openweatherapp.api.RetroFitInstance
import com.iuturakulov.openweatherapp.utils.FailureState
import com.iuturakulov.openweatherapp.utils.StatusState
import com.iuturakulov.openweatherapp.utils.SuccessState
import javax.inject.Inject

class WeatherInfoShowModelImpl @Inject constructor() : WeatherInfoShowModel {
    override suspend fun getWeatherInfo(
        cityName: String,
    ): StatusState {

        val apiInterface: ApiHelperImpl = RetroFitInstance.client.create(ApiHelperImpl::class.java)

        return try {
            SuccessState(apiInterface.callForCityWeather(cityName, BuildConfig.APP_ID))
        } catch (cause: Throwable) {
            FailureState(msg = "Network call failure ${cause.localizedMessage}")
        }
    }
}