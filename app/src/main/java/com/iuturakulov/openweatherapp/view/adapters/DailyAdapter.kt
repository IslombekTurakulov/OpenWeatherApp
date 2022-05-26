package com.iuturakulov.openweatherapp.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iuturakulov.openweatherapp.R
import com.iuturakulov.openweatherapp.model.models.DailyForecast
import kotlinx.android.synthetic.main.next_weather_item.view.*
import timber.log.Timber

class DailyAdapter(
    val context: Context,
    var dailyForecast: List<DailyForecast>
) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: ViewHolder? = null
        try {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.next_weather_item, parent, false)
            viewHolder = ViewHolder(context, view)
        } catch (e: Exception) {
            Timber.e(e.message!!)
        }
        return viewHolder!!
    }

    override fun getItemCount() = dailyForecast.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = dailyForecast[position]
        holder.bind(forecast)
    }

    class ViewHolder(
        val context: Context,
        val view: View,
    ) : RecyclerView.ViewHolder(view.rootView) {
        fun bind(forecast: DailyForecast) {
            with(forecast) {
                itemView.nextDayText.text = this.weather[0].main
                val weatherIconUrl =
                    "https://openweathermap.org/img/wn/${this.weather[0].icon}@2x.png"
                Glide.with(context)
                    .load(weatherIconUrl)
                    .override(150, 150)
                    .fitCenter()
                    .into(view.nextIcon)
            }
        }
    }
}