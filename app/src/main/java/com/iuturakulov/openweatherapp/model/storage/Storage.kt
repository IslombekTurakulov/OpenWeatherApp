package com.iuturakulov.openweatherapp.model.storage

interface Storage {
    fun saveData(key: String, value: String)
    fun saveBool(key: String, value: Boolean)
    fun getData(key: String): String
    fun getBool(key: String): Boolean
}