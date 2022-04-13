package com.iuturakulov.openweatherapp.data.model.models

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("id")
    var id = 0

    @SerializedName("main")
    var main: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("icon")
    var icon: String? = null
}