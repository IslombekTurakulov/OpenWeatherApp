package com.iuturakulov.openweatherapp.model.database

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.iuturakulov.openweatherapp.di.App
import com.iuturakulov.openweatherapp.model.models.Place

object PlaceDao {

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() =
        App.context.getSharedPreferences("app_weather", Context.MODE_PRIVATE)

}