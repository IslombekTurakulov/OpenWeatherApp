package com.iuturakulov.openweatherapp.model.storage

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.iuturakulov.openweatherapp.di.App
import com.iuturakulov.openweatherapp.model.models.Place
import com.iuturakulov.openweatherapp.model.models.SearchResults
import com.iuturakulov.openweatherapp.utils.STORAGE_NAME

object PlaceDao {
    fun savePlace(place: SearchResults) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): SearchResults? {
        val placeJson = sharedPreferences().getString("place", "")
        if (placeJson.isNullOrEmpty()) {
            return null
        }
        return Gson().fromJson(placeJson, SearchResults::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")
    private fun sharedPreferences() =
        App.context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
}