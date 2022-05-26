package com.iuturakulov.openweatherapp.model.models

import com.google.gson.annotations.SerializedName

class Wind {
    @SerializedName("speed")
    var speed = 0f

    @SerializedName("deg")
    var deg = 0f
}