package com.tkadela.capitalsweather.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tkadela.capitalsweather.domain.WeatherData

/**
 * ViewModelFactory for ForecastDetailViewModel
 *
 * Custom ViewModelFactory is required to pass parameters to the ViewModel
 */
class ForecastDetailViewModelFactory(private val weatherData: WeatherData)
            : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastDetailViewModel::class.java)) {
            return ForecastDetailViewModel(weatherData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}