package com.iuturakulov.openweatherapp.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iuturakulov.openweatherapp.MyApp
import com.iuturakulov.openweatherapp.R
import com.iuturakulov.openweatherapp.model.models.HourlyDailyData
import com.iuturakulov.openweatherapp.utils.GpsUtils
import com.iuturakulov.openweatherapp.utils.LocationUtils
import com.iuturakulov.openweatherapp.utils.kelvinToCelsius
import com.iuturakulov.openweatherapp.utils.unixTimestampToDateTimeString
import com.iuturakulov.openweatherapp.view.adapters.DailyAdapter
import com.iuturakulov.openweatherapp.view.adapters.HourlyAdapter
import com.iuturakulov.openweatherapp.viewModel.viewModels.WeatherInfoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var weatherInfoViewModel: WeatherInfoViewModel
    lateinit var locUtils: LocationUtils

    private val REQUEST_LOCATION_CODE = 1
    private var isGps = false
    private lateinit var location: String
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var hourlyAdapter: HourlyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as MyApp).appComponent.inject(this)
        /*  weatherInfoViewModel = WeatherInfoViewModelFactory(this.application as MyApp).create(
              WeatherInfoViewModel::class.java
          )*/
        locUtils = LocationUtils(this)
        GpsUtils(this).turnOnGps(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnabled: Boolean) {
                isGps = isGPSEnabled
                if (isGps) {
                    getLocation()
                }
            }
        })
        current_location.setOnClickListener {
            getLocation()
        }
        weatherInfoViewModel.weatherInfoLiveData.observe(this) {
            setWeatherInfo(it)
        }
    }


    private fun getLocation() {
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            locUtils.address.observeOnce(this) {
                weatherRequest(it as MutableList<Address>)
            }
        } else
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_CODE
            )
    }

    private val weatherRequest: (address: MutableList<Address>) -> Unit = {
        val local = it[0].locality
        val country = it[0].countryName
        location = "$local, $country"
        cityNameText.text = "$local $country"
        initObserver(it[0].latitude, it[0].longitude)
    }

    private fun initObserver(latitude: Double, longitude: Double) {
        weatherInfoViewModel.getWeatherInfo(latitude, longitude)
        weatherInfoViewModel.weatherInfoLiveData.observe(this) {
            setWeatherInfo(it)
            dailyAdapter = DailyAdapter(this, it.daily)
            hourlyAdapter = HourlyAdapter(this, it.hourly)
        }
    }

    private fun setWeatherInfo(info: HourlyDailyData) {
        tempText.text = info.currentWeather.temp.kelvinToCelsius().toString()
        cityNameText.text =
            "${info.timezone}, ${info.currentWeather.dt.unixTimestampToDateTimeString()}"
        Glide.with(this).load(info.currentWeather.weather[0].icon.replace("http", "https"))
            .into(curConditionIcon)
        conditionText.text = info.currentWeather.weather[0].description
        feelsLikeText.text =
            "${getString(R.string.feels_like_30)} ${info.currentWeather.feelsLike.kelvinToCelsius()}"
        humidityText.text = "${info.currentWeather.humidity} %"
        windText.text = "${info.currentWeather.windSpeed}  km/h"
        maxText.text = info.daily[0].temp.max.kelvinToCelsius().toString()
        minText.text = info.daily[0].temp.min.kelvinToCelsius().toString()
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}
