package com.iuturakulov.openweatherapp.viewModel.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iuturakulov.openweatherapp.BuildConfig
import com.iuturakulov.openweatherapp.model.models.SearchResults
import com.iuturakulov.openweatherapp.model.models.Weather
import com.iuturakulov.openweatherapp.utils.NetworkHelper
import com.iuturakulov.openweatherapp.utils.Resource
import com.iuturakulov.openweatherapp.viewModel.repositories.WeatherRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException

class WeatherInfoViewModel @ViewModelInject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: WeatherRepositoryImpl
) : ViewModel() {

    private val weatherListData = MutableLiveData<Resource<Response<Weather>>>()
    private val searchedListData = MutableLiveData<Resource<Response<SearchResults>>>()

    val weatherData: LiveData<Resource<Response<Weather>>>
        get() = weatherListData

    val searchData: LiveData<Resource<Response<SearchResults>>>
        get() = searchedListData

    fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherListData.postValue(Resource.loading(null))
            if (networkHelper.isConnected()) {
                try {
                    weatherListData.postValue(
                        Resource.success(
                            repository.getWeatherData(
                                latitude,
                                longitude,
                                BuildConfig.APP_ID
                            )
                        )
                    )
                } catch (ex: HttpException) {
                    weatherListData.postValue(Resource.error(null, ex.code().toString()))
                } catch (ex: ConnectException) {
                    weatherListData.postValue(
                        Resource.error(
                            null,
                            "Please check your internet connection!"
                        )
                    )
                } catch (ex: SocketTimeoutException) {
                    weatherListData.postValue(Resource.error(null, "Connection timed out!"))
                } catch (ex: SocketException) {
                    weatherListData.postValue(
                        Resource.error(
                            null,
                            "Please check your internet connection!"
                        )
                    )
                }
            } else {
                weatherListData.postValue(Resource.error(null, "No internet connection"))
            }
        }
    }

    fun searchLocationData(locationName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchedListData.postValue(Resource.loading(null))
            if (networkHelper.isConnected()) {
                try {
                    searchedListData.postValue(
                        Resource.success(
                            repository.getSearchLocationData(
                                locationName,
                                BuildConfig.APP_ID
                            )
                        )
                    )
                } catch (ex: HttpException) {
                    weatherListData.postValue(Resource.error(null, ex.code().toString()))
                } catch (ex: ConnectException) {
                    weatherListData.postValue(
                        Resource.error(
                            null,
                            "Please check your internet connection!"
                        )
                    )
                } catch (ex: SocketTimeoutException) {
                    weatherListData.postValue(Resource.error(null, "Connection timed out!"))
                } catch (ex: SocketException) {
                    weatherListData.postValue(
                        Resource.error(
                            null,
                            "Please check your internet connection!"
                        )
                    )
                }
            } else {
                weatherListData.postValue(Resource.error(null, "No internet connection"))
            }
        }
    }
}