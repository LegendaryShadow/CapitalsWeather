package com.tkadela.capitalsweather.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModelFactory for WeatherListViewModel
 *
 * Custom ViewModelFactory is required to pass parameters to the ViewModel
 */
class WeatherListViewModelFactory(private val app: Application,
                                  private val isLocationInitialized: Boolean)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherListViewModel::class.java)) {
            return WeatherListViewModel(app, isLocationInitialized) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}