package com.iuturakulov.openweatherapp.api

import android.app.appsearch.SearchResults
import com.iuturakulov.openweatherapp.model.models.Weather
import com.iuturakulov.openweatherapp.model.models.WeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


interface ApiService {
    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") unit: String
    ) : Response<Weather>

    @GET("weather")
    suspend fun searchWeatherLocationData(
        @Query("q") locationName: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String
    ) : Response<SearchResults>

    @GET("weather")
    suspend fun callForCityWeather(
        @Query("q") cityName: String,
        @Query("appid") myAppId: String
    ): WeatherForecast
}