package com.iuturakulov.openweatherapp.viewModel.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iuturakulov.openweatherapp.model.models.HourlyDailyData
import com.iuturakulov.openweatherapp.storage.SharedPreferencesStorage
import com.iuturakulov.openweatherapp.utils.StatusState
import com.iuturakulov.openweatherapp.utils.SuccessState
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherInfoViewModel @Inject constructor(
    private val weatherModel: WeatherInfoRequestModelImpl,
    private val sharedPreferencesStorage: SharedPreferencesStorage
) : ViewModel() {

    val weatherInfoLiveData = MutableLiveData<HourlyDailyData>()

    fun getWeatherInfo(lat: Double, lon: Double) {
        viewModelScope.launch {
            when (val res: StatusState =
                weatherModel.getWeatherInfo(lat, lon)) {
                is SuccessState -> {
                    val weatherData = HourlyDailyData(
                        lat = res.response.lat,
                        lon = res.response.lon,
                        timezone = res.response.timezone,
                        timezoneOffset = res.response.timezoneOffset,
                        currentWeather = res.response.currentWeather,
                        hourly = res.response.hourly,
                        daily = res.response.daily
                    )
                    weatherInfoLiveData.postValue(weatherData)
                }
                else -> {
                    Timber.d("Failure")
                }
            }
        }
    }
}