package com.iuturakulov.openweatherapp.viewModel.viewModels

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iuturakulov.openweatherapp.model.models.Weather
import com.iuturakulov.openweatherapp.model.models.WeatherData
import com.iuturakulov.openweatherapp.storage.SharedPreferencesStorage
import com.iuturakulov.openweatherapp.utils.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherInfoViewModel @Inject constructor(
    private val weatherModel: WeatherInfoShowModelImpl,
    private val sharedPreferencesStorage: SharedPreferencesStorage
) : ViewModel() {

    private val cityListLiveData = MutableLiveData<MutableList<String>>()
    private val progressBarLiveData = MutableLiveData<Boolean>()
    private val weatherInfoFailureLiveData = MutableLiveData<String>()
    private val weatherInfoLiveData = MutableLiveData<WeatherData>()
    private val weatherDailyLiveData = MutableLiveData<List<Weather>>()

    val getWeatherInfoLiveData: MutableLiveData<WeatherData> = weatherInfoLiveData
    val getWeatherHourlyLiveData: MutableLiveData<List<Weather>> = weatherDailyLiveData

    fun getWeatherInfo(cityName: String) {
        viewModelScope.launch {
            progressBarLiveData.postValue(true)
            when (val res: StatusState =
                weatherModel.getWeatherInfo(cityName)) {
                is FailureState -> {
                    weatherInfoFailureLiveData.postValue(res.msg)
                }
                is SuccessState -> {
                    val weatherData = WeatherData(
                        dateTime = res.response.dt.unixTimestampToDateTimeString(),
                        temperature = res.response.main.temp.kelvinToCelsius().toString(),
                        cityAndCountry = "${res.response.name}, ${res.response.sys.country}",
                        weatherConditionIconUrl = "http://openweathermap.org/img/w/${res.response.weather[0].icon}.png",
                        weatherConditionIconDescription = res.response.weather[0].description,
                        humidity = "${res.response.main.humidity} %",
                        wind = "${res.response.wind} km/h",
                        sunrise = res.response.sys.sunrise.unixTimestampToTimeString(),
                        sunset = res.response.sys.sunset.unixTimestampToTimeString(),
                        feelsLike = res.response.main.feelsLike.kelvinToCelsius().toString(),
                        tempMin = res.response.main.tempMin.kelvinToCelsius().toString(),
                        tempMax = res.response.main.tempMax.kelvinToCelsius().toString(),
                    )
                    weatherInfoLiveData.postValue(weatherData)
                }
            }
            progressBarLiveData.postValue(false)
        }
    }

    fun getByCityName(cityName: String) {
        viewModelScope.launch {
            progressBarLiveData.postValue(true)
            when (val res = weatherModel.getByLonAndLat(cityName)) {
                is FailureState -> {
                    weatherInfoFailureLiveData.postValue(res.msg)
                }
                is SuccessResultState -> {
                    weatherDailyLiveData.postValue(res.response.body()?.weather)
                }
                else -> {

                }
            }
            progressBarLiveData.postValue(false)
        }
    }

    fun loadTheme(): Boolean {
        return sharedPreferencesStorage.getBool(THEME_KEY)
    }

    private fun loadCityFromCache(): Int {
        val keyCity = sharedPreferencesStorage.getData(CITY_KEY)
        var resultId: Int? = 0
        if (keyCity.isEmpty()) {
            resultId = cityListLiveData.value?.indexOf(keyCity)
            if (resultId == -1) {
                resultId = 0
            } else if (resultId == null) {
                resultId = 0
            }
        }
        Timber.d("$resultId")
        return resultId!!
    }

    fun saveChosenCity(cityId: Int) {
        sharedPreferencesStorage.saveData(CITY_KEY, cityListLiveData.value!![cityId])
        Timber.d("city: ")
    }
}