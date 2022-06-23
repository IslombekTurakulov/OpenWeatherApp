package com.iuturakulov.openweatherapp.model.storage

import com.iuturakulov.openweatherapp.model.models.SearchResults

object RepositoryDAO {
    fun savePlace(place: SearchResults) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}