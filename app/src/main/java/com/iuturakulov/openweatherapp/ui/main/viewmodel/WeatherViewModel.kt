package com.iuturakulov.openweatherapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iuturakulov.openweatherapp.data.model.models.WeatherForecast
import com.iuturakulov.openweatherapp.data.repository.WeatherRepository
import com.iuturakulov.openweatherapp.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WeatherViewModel(private val mainRepository: WeatherRepository) : ViewModel() {

    private val weatherForecast = MutableLiveData<Resource<List<WeatherForecast>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchWeatherForecast()
    }

    private fun fetchWeatherForecast() {
        weatherForecast.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userList ->
                    weatherForecast.postValue(Resource.success(userList))
                }, {
                    weatherForecast.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getWeatherForecast(): LiveData<Resource<List<WeatherForecast>>> {
        return weatherForecast
    }

}