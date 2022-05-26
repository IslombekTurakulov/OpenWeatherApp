package com.iuturakulov.openweatherapp.model.models

import com.google.gson.annotations.SerializedName

class Main {
    @SerializedName("temp")
    var temp = 0.0

    @SerializedName("humidity")
    var humidity = 0f

    @SerializedName("pressure")
    var pressure = 0f

    @SerializedName("temp_min")
    var temp_min = 0f

    @SerializedName("temp_max")
    var temp_max = 0f
}