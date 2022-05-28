package com.iuturakulov.openweatherapp.api

import com.iuturakulov.openweatherapp.model.models.SearchResults
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
    ) : Response<Weather>

    @GET("weather")
    suspend fun searchWeatherLocationData(
        @Query("q") locationName: String,
        @Query("appid") apiKey: String,
    ) : Response<SearchResults>
}