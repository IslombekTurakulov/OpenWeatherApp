package com.iuturakulov.openweatherapp.model.storage

import com.iuturakulov.openweatherapp.model.models.Weather

object RepositoryDAO {
    fun savePlace(place: Weather) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}