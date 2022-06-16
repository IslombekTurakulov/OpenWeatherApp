package com.iuturakulov.openweatherapp.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iuturakulov.openweatherapp.R
import com.iuturakulov.openweatherapp.model.models.SearchResults
import com.iuturakulov.openweatherapp.model.models.Weather
import com.iuturakulov.openweatherapp.utils.*
import com.iuturakulov.openweatherapp.view.adapters.DailyAdapter
import com.iuturakulov.openweatherapp.view.adapters.HourlyAdapter
import com.iuturakulov.openweatherapp.viewModel.viewModels.WeatherInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_weather_info.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class WeatherInfoFragment : Fragment() {

    private val REQUEST_LOCATION_CODE = 1
    private val weatherInfoViewModel: WeatherInfoViewModel by viewModels()

    @Inject
    lateinit var locUtils: LocationUtils

    private var isGps = false
    private lateinit var location: String
    private lateinit var city: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_info, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GpsUtils(requireActivity()).turnOnGps(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnabled: Boolean) {
                isGps = isGPSEnabled
                when {
                    isGps -> getLocation()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar.setOnEditorActionListener(
            TextView.OnEditorActionListener { v, actionId, event ->
                if ((actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) && (event == null || !event.isShiftPressed)) {
                    city = searchBar.text.toString()
                    initSearchObserver(city)
                    return@OnEditorActionListener true
                }
                false
            })

        current_location.setOnClickListener {
            getLocation()
        }
    }

    private fun getLocation() {
        if ((ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            locUtils.address.observeOnce(this) {
                weatherRequest(it as MutableList<Address>)
            }
        } else
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_CODE
            )
    }

    private val weatherRequest: (address: MutableList<Address>) -> Unit = {
        val local = it[0].locality
        val country = it[0].countryName
        location = "$local, $country"
        cityNameText.text = local
        initObserver(location, it[0].latitude, it[0].longitude)
    }

    private fun initObserver(location: String, latitude: Double, longitude: Double) {
        weatherInfoViewModel.getWeatherData(latitude, longitude)
        weatherInfoViewModel.weatherData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    bindViews(it.data?.body()!!)
                }
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindViews(data: Weather) {
        with(data) {
            val hourlyAdapter = HourlyAdapter(requireContext(), this.hourly.subList(0, 24))
            val dailyAdapter = DailyAdapter(requireContext(), this.daily)
            rvWeather.adapter = hourlyAdapter
            rvNextWeather.adapter = dailyAdapter
            rvWeather.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvNextWeather.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            tempText.text = "${data.current.temp.kelvinToCelsius()}"
            conditionText.text = this.current.weather[0].main
            feelsLikeText.text =
                "${getString(R.string.feels_like_30)} ${this.current.feelsLike.kelvinToCelsius()}"
            humidityText.text = "${this.current.humidity} %"
            windText.text = "${this.current.windSpeed} km/h"
            maxText.text = "max ${this.daily[0].temp.max.kelvinToCelsius()}°/"
            minText.text = "min ${this.daily[0].temp.min.kelvinToCelsius()}°"
            val icon = this.current.weather[0].icon
            val weatherIconUrl = "https://openweathermap.org/img/wn/$icon@4x.png"
            Glide.with(this@WeatherInfoFragment)
                .load(weatherIconUrl)
                .override(150, 150)
                .fitCenter()
                .into(curConditionIcon)
        }
    }

    private fun initSearchObserver(locationName: String) {
        weatherInfoViewModel.searchLocationData(locationName)
        weatherInfoViewModel.searchData.observe(viewLifecycleOwner) {
            try {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        bindViews(it.data?.body()!!)
                    }
                    Status.LOADING -> {
                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "Error...", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: NullPointerException) {
                Timber.e(e)
            }
        }
    }

    private fun bindViews(searchResults: SearchResults) {
        with(searchResults) {
            tempText.text = this.main.temp.kelvinToCelsius().toString()
            cityNameText.text = "${this.name}, ${this.sys.country}"
            conditionText.text = this.weather[0].main
            feelsLikeText.text =
                "${getString(R.string.feels_like_30)} ${this.main.feelsLike.kelvinToCelsius()}"
            humidityText.text = "${this.main.humidity} %"
            windText.text = "${this.wind.speed} km/h"
            maxText.text = "max ${this.main.tempMax.kelvinToCelsius()}°/"
            minText.text = "min ${this.main.tempMin.kelvinToCelsius()}°"
            val icon = this.weather[0].icon
            val weatherIconUrl = "https://openweathermap.org/img/wn/$icon@4x.png"
            Glide.with(this@WeatherInfoFragment)
                .load(weatherIconUrl)
                .override(150, 150)
                .fitCenter()
                .into(curConditionIcon)
        }
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
