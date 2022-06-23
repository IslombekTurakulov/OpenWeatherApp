package com.iuturakulov.openweatherapp.model.storage

import com.iuturakulov.openweatherapp.model.models.Place

object RepositoryDAO {
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}