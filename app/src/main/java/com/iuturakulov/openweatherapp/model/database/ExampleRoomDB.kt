/*
package com.iuturakulov.openweatherapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iuturakulov.openweatherapp.model.database.daos.ExampleDAO
import com.iuturakulov.openweatherapp.model.models.WeatherForecast
import com.iuturakulov.openweatherapp.utils.ROOM_DB_NAME

@Database(entities = [WeatherForecast::class], version = 1, exportSchema = false)
// Not used.
abstract class ExampleRoomDB : RoomDatabase() {

    abstract fun getEmployeeDao(): ExampleDAO

    companion object {
        private var INSTANCE: ExampleRoomDB? = null

        fun getDatabase(context: Context) = INSTANCE ?: kotlin.run {
            Room.databaseBuilder(
                context.applicationContext,
                ExampleRoomDB::class.java,
                ROOM_DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
*/
