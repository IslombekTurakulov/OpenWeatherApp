package com.iuturakulov.openweatherapp.data.api

import com.iuturakulov.openweatherapp.data.model.models.WeatherForecast
import com.iuturakulov.openweatherapp.utils.BASE_URL
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

class ApiServiceImpl : ApiService {
    override fun getCurrentWeather(): Single<List<WeatherForecast>> {
        return Rx2AndroidNetworking.get(BASE_URL)
            .build()
            .getObjectListSingle(WeatherForecast::class.java)
    }
}