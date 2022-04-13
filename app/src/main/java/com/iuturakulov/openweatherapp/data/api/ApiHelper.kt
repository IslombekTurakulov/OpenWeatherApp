package com.iuturakulov.openweatherapp.data.api

class ApiHelper(private val apiService: ApiService) {

    fun getCurrentWeather() = apiService.getCurrentWeather()

}