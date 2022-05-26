package com.iuturakulov.openweatherapp.api

import com.iuturakulov.openweatherapp.model.models.HourlyDaily
import com.iuturakulov.openweatherapp.model.models.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String = "daily, hourly",
    ): HourlyDaily
}