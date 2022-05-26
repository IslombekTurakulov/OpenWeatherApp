/*
package com.iuturakulov.openweatherapp.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iuturakulov.openweatherapp.MyApp
import com.iuturakulov.openweatherapp.R
import com.iuturakulov.openweatherapp.model.models.WeatherData
import com.iuturakulov.openweatherapp.view.adapters.DailyAdapter
import com.iuturakulov.openweatherapp.view.adapters.HourlyAdapter
import com.iuturakulov.openweatherapp.viewModel.viewModels.WeatherInfoViewModel
import com.iuturakulov.openweatherapp.viewModel.viewModels.WeatherInfoViewModelFactory
import kotlinx.android.synthetic.main.fragment_weather_info.*
import javax.inject.Inject


class WeatherInfoFragment : Fragment() {

    @Inject
    lateinit var weatherInfoViewModel: WeatherInfoViewModel

    private val REQUEST_LOCATION_CODE = 1
    private var isGps = false
    private lateinit var location: String
    private lateinit var dailyAdapter : DailyAdapter
    private lateinit var hourlyAdapter : HourlyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity().application as MyApp).appComponent.inject(this)
        return inflater.inflate(R.layout.fragment_weather_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
*/
/*        GpsUtils(requireActivity()).turnOnGps(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnabled: Boolean) {
                isGps = isGPSEnabled
                if (isGps)
                    getLocation()
            }
        })*//*

        // locUtils = LocationUtils(requireContext())
        weatherInfoViewModel = WeatherInfoViewModelFactory(requireActivity().application as MyApp).create(WeatherInfoViewModel::class.java)
        current_location.setOnClickListener {
                getLocation()
        }
        val country = "Moscow"
        weatherInfoViewModel.getWeatherInfo(country)
        dailyAdapter = DailyAdapter(requireContext(), arrayListOf())
        hourlyAdapter = HourlyAdapter(requireContext(), arrayListOf())
        rvNextWeather.adapter = dailyAdapter
        rvWeather.adapter = hourlyAdapter
    }

    val weatherRequest: (address: MutableList<Address>) -> Unit = {
        */
/* val lat = it[0].latitude
         val long = it[0].longitude
         val locality = it[0].locality*//*

        // val country = it[0].countryName
        val country = "Moscow"
        weatherInfoViewModel.getWeatherInfo(country)
        weatherInfoViewModel.getByCityName(country)
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
           */
/* /locUtils.address.observeOnce(this@WeatherInfoFragment) {
                weatherRequest(it as MutableList<Address>)
            }*//*

        } else
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_CODE
            )
    }

    private fun setWeatherInfo(info: WeatherData) {
        tempText.text = info.temperature
        cityNameText.text = "${info.cityAndCountry}, ${info.dateTime}"
        Glide.with(requireContext()).load(info.weatherConditionIconUrl.replace("http", "https"))
            .into(curConditionIcon)
        conditionText.text = info.weatherConditionIconDescription
        feelsLikeText.text = info.feelsLike
        humidityText.text = info.humidity
        windText.text = info.wind
        maxText.text = info.tempMax
        minText.text = info.tempMin
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}*/
