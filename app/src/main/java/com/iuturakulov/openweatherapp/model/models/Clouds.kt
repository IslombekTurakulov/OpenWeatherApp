package com.iuturakulov.openweatherapp.model.models

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Long
)
