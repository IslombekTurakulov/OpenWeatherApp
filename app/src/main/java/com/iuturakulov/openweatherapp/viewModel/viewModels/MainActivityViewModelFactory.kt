package com.iuturakulov.openweatherapp.viewModel.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iuturakulov.openweatherapp.App

class MainActivityViewModelFactory(private val application: App) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(application.weatherModel, application.preferenceStorage) as T
    }
}