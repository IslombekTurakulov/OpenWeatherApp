package com.iuturakulov.openweatherapp.model.models


data class HourlyDailyData(
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezoneOffset: Int,
    var currentWeather: Current,
    var hourly: List<HourlyForecast>,
    var daily: List<DailyForecast>
)