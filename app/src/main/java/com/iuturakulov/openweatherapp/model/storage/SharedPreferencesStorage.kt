package com.iuturakulov.openweatherapp.model.storage

import android.content.Context
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(context: Context): Storage {

    private val sharedPreferences = context.getSharedPreferences("appStorage", Context.MODE_PRIVATE)

    override fun saveData(key: String, value: String) {
        with(sharedPreferences.edit())
        {
            putString(key, value)
            apply()
        }
    }

    override fun saveBool(key: String, value: Boolean) {
        with(sharedPreferences.edit())
        {
            putBoolean(key, value)
            apply()
        }
    }

    override fun getData(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    override fun getBool(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

}