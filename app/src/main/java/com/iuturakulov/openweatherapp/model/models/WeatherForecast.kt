package com.iuturakulov.openweatherapp.model.models

import com.google.gson.annotations.SerializedName

class WeatherForecast {
    @SerializedName("coord")
    var coord: Coord? = null

    @SerializedName("sys")
    var sys: Sys? = null

    @SerializedName("weather")
    var weather: ArrayList<Weather> = arrayListOf()

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("rain")
    var rain: Rain? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("dt")
    var dt = 0L

    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("cod")
    var cod = 0f
}