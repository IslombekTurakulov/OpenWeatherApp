package com.iuturakulov.openweatherapp.model.models

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("feelsLike")
    val feelsLike: Double,
    @SerializedName("tempMin")
    val tempMin: Double,
    @SerializedName("tempMax")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Long,
    @SerializedName("humidity")
    val humidity: Long
)