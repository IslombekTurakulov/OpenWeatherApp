package com.iuturakulov.openweatherapp.view.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    private var currentChosenWeather: Weather? = null

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
                    if (city.isNotEmpty()) {
                        initSearchObserver(city)
                        return@OnEditorActionListener true
                    } else {
                        return@OnEditorActionListener false
                    }
                }
                false
            })
        shareTodayWeatherForecast.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareSub = "Today Weather"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareTodayWeather())
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
        shareNextWeatherForecast.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareSub = "Next Weather"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareNextWeather())
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
        current_location.setOnClickListener {
            getLocation()
        }
    }

    private fun shareTodayWeather(): String {
        val builder: StringBuilder = java.lang.StringBuilder()
        builder.append("${getString(R.string.weather_country)} ${cityNameText.text}\n")
        for (hourlyWeather in currentChosenWeather!!.hourly) {
            builder.append(
                "\n${hourlyWeather.dt.convertTimeStampToHour()}: ${getString(R.string.weather_condition)} ${hourlyWeather.weather[0].main}, ${hourlyWeather.temp.kelvinToCelsius()}°, ${
                    getString(
                        R.string.feels_like_30
                    )
                } ${hourlyWeather.feelsLike.kelvinToCelsius()} °"
            )
        }
        return builder.toString()
    }

    private fun shareNextWeather(): String {
        val builder: StringBuilder = java.lang.StringBuilder()
        builder.append("${getString(R.string.weather_country)} ${cityNameText.text}\n")
        for (hourlyWeather in currentChosenWeather!!.daily) {
            builder.append(
                "\n${hourlyWeather.dt.convertTimeStampToDay()}: ${getString(R.string.weather_condition)} ${hourlyWeather.weather[0].main}, ${hourlyWeather.temp.day.kelvinToCelsius()}°, ${
                    getString(
                        R.string.feels_like_30
                    )
                } ${hourlyWeather.feelsLike.day.kelvinToCelsius()} °"
            )
        }
        return builder.toString()
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
                    currentChosenWeather = it.data.body()!!
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
            weatherConditionRecyclerView.adapter = hourlyAdapter
            rvNextWeather.adapter = dailyAdapter
            weatherConditionRecyclerView.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvNextWeather.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            tempText.text = "${data.current.temp.kelvinToCelsius()}"
            conditionText.text = this.current.weather[0].main
            feelsLikeText.text =
                "${getString(R.string.feels_like_30)} ${this.current.feelsLike.kelvinToCelsius()}°"
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
            initializeLineChartTemperature()
        }
    }

    private fun Weather.initializeLineChartTemperature() {
        val prevData = nextDayTemperatureLineChart.data
        if (prevData != null) {
            nextDayTemperatureLineChart.clear()
            nextDayTemperatureLineChart.zoom(0f, 0f, 0f, 0f)
        }
        val entries = mutableListOf<Entry>()
        val referenceTimestamp = this.hourly[0].dt
        val newTimestamps = mutableListOf<Long>()
        for (i in this.hourly.indices) {
            if (i != 24) {
                val newDt = this.hourly[i].dt - referenceTimestamp
                entries.add(Entry(newDt.toFloat(), this.hourly[i].temp.kelvinToCelsius().toFloat()))
                newTimestamps.add(newDt)

                Timber.e("Time: ${newDt.convertTimeStampToDay()}")
                continue
            }
            break
        }
        Timber.e(entries.size.toString())
        val lineDataset = LineDataSet(entries, "Hourly temperature")
        lineDataset.axisDependency = YAxis.AxisDependency.LEFT
        lineDataset.valueTextColor = Color.WHITE

        lineDataset.setCircleColor(Color.WHITE)
        lineDataset.setDrawCircleHole(false)
        lineDataset.setDrawFilled(true)

        lineDataset.fillColor = Color.parseColor("#03DAC5")
        lineDataset.fillAlpha = 50
        lineDataset.setDrawHighlightIndicators(false)
        lineDataset.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataset.valueTextSize = 14f
        lineDataset.cubicIntensity = 0.4f
        lineDataset.circleRadius = 2f
        lineDataset.enableDashedLine(15f, 10f, 0f)
        lineDataset.valueFormatter = object : ValueFormatter() {

            override fun getPointLabel(entry: Entry?): String {
                return "${(entry!!.y).toInt()}°"
            }

            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toLong().convertTimeStampToHour()
            }
        }

        val dataSet = listOf<ILineDataSet>(lineDataset)
        val lineData = LineData(dataSet)
        nextDayTemperatureLineChart.data = lineData

        val xAxis = nextDayTemperatureLineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 12f
        xAxis.textColor = Color.WHITE
        // xAxis.textSize = 4f
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.setLabelCount(11, true)
        xAxis.valueFormatter = object : ValueFormatter() {

            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                Timber.e(value.toString())
                val index = (value / 10000).toInt()
                return (referenceTimestamp + newTimestamps[index]).convertTimeStampToHour()
            }
        }

        val yAxis = nextDayTemperatureLineChart.axisLeft
        yAxis.textSize = 14f
        yAxis.isGranularityEnabled = true
        yAxis.granularity = 4f
        val chartDesc = nextDayTemperatureLineChart.description
        chartDesc.text = "Hourly temperature"
        chartDesc.textColor = Color.WHITE
        chartDesc.textSize = 13f
        nextDayTemperatureLineChart.legend.isEnabled = false
        nextDayTemperatureLineChart.axisRight.isEnabled = false
        nextDayTemperatureLineChart.axisLeft.isEnabled = false
        nextDayTemperatureLineChart.zoom(2f, 0f, 1f, 1f)
        nextDayTemperatureLineChart.setPinchZoom(true)
        nextDayTemperatureLineChart.isScaleXEnabled = false
        nextDayTemperatureLineChart.isScaleYEnabled = false
        nextDayTemperatureLineChart.animateXY(3000, 3000, Easing.EaseInCubic)
        nextDayTemperatureLineChart.invalidate()
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
            initObserver(this.name, this.coord.lat, this.coord.lon)
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
