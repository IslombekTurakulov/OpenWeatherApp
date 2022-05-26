package com.iuturakulov.openweatherapp.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iuturakulov.openweatherapp.R
import com.iuturakulov.openweatherapp.model.models.Weather
import kotlinx.android.synthetic.main.next_weather_item.view.*
import timber.log.Timber

class DailyAdapter(
    val context: Context,
    var dailyForecast: ArrayList<Weather>
) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vh: ViewHolder? = null
        try {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.next_weather_item, parent, false)
            vh = ViewHolder(context, view)
        } catch (e: Exception) {
            Timber.e(e.message!!)
        }
        return vh!!
    }

    override fun getItemCount() = dailyForecast.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = dailyForecast[position]
        holder.bind(forecast)
    }

    fun addData(users: List<Weather>) {
        dailyForecast.addAll(users)
    }

    class ViewHolder(
        val context: Context,
        val view: View,
    ) : RecyclerView.ViewHolder(view.rootView) {
        fun bind(forecast: Weather) {
            with(forecast) {
                itemView.nextDayText.text = this.main
                val weatherIconUrl =
                    "https://openweathermap.org/img/wn/${this.icon}@2x.png"
                Glide.with(context)
                    .load(weatherIconUrl)
                    .override(150, 150)
                    .fitCenter()
                    .into(view.nextIcon)
            }
        }
    }
}