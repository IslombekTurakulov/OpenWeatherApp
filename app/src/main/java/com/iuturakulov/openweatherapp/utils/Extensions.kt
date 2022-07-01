package com.iuturakulov.openweatherapp.utils

import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.util.*

fun setTheme(flag: Boolean) = when {
    flag -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}


fun Double.kelvinToCelsius(): Int {
    return (this - 273.15).toInt()
}

fun Long.unixTimestampToTimeString(): String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun Long.convertTimeStampToHour(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this * 1000 // date
    val timeZone = TimeZone.getDefault()
    calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.timeInMillis))

    val simpleDateFormat = SimpleDateFormat("hha", Locale.getDefault())

    return simpleDateFormat.format(calendar.time)
}


fun Long.convertTimeStampToDay(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this * 1000
    val timeZone = TimeZone.getDefault()
    calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.timeInMillis))
    val simpleDateFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val date = calendar.get(Calendar.DATE)
    val day = simpleDateFormat.format(calendar.time)
    return "$day $date'"
}