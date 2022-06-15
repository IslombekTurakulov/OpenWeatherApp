package com.iuturakulov.openweatherapp.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.iuturakulov.openweatherapp.R
import com.iuturakulov.openweatherapp.model.models.HourlyForecast
import com.iuturakulov.openweatherapp.utils.kelvinToCelsius
import com.iuturakulov.openweatherapp.utils.unixTimestampToTimeString
import kotlinx.android.synthetic.main.weather_item.view.*
import timber.log.Timber

@GlideModule
class HourlyAdapter(
    val context: Context,
    private val hourlyForecast: List<HourlyForecast>
) : RecyclerView.Adapter<HourlyAdapter.HourlyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyHolder {
        var vh: HourlyHolder? = null
        try {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false)
            vh = HourlyHolder(context, view)
        } catch (e: Exception) {
            Timber.e(e.message!!)
        }
        return vh!!
    }

    override fun getItemCount(): Int = hourlyForecast.size

    override fun onBindViewHolder(holder: HourlyHolder, position: Int) {
        val forecast = hourlyForecast[position]
        holder.bind(forecast)
    }

    class HourlyHolder(val context: Context, val view: View) :
        RecyclerView.ViewHolder(view.rootView) {
        fun bind(hourlyForecast: HourlyForecast) {
            with(hourlyForecast) {
                itemView.cardTime.text = this.dt.unixTimestampToTimeString()
                itemView.cardWind.text = "${this.windSpeed} km/h"
                itemView.cardTemp.text = "${this.temp.kelvinToCelsius()}°"
                val icon = this.weather[0].icon
                val weatherIconUrl = "https://openweathermap.org/img/wn/$icon@4x.png"
                Glide.with(context)
                    .load(weatherIconUrl)
                    .override(150, 150)
                    .fitCenter()
                    .into(view.cardIcon)
            }
        }
    }
}