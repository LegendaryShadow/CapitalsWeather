package com.tkadela.capitalsweather.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tkadela.capitalsweather.domain.WeatherData

class ForecastDetailViewModel(weatherData: WeatherData) : ViewModel() {

    /**
     * Variable containing the weather data for this Fragment
     *
     * Contains the forecasts to be used in the RecyclerView
     */
    private val _selectedWeatherData = MutableLiveData<WeatherData>()
    val selectedWeatherData: LiveData<WeatherData>
        get() = _selectedWeatherData

    init {
        _selectedWeatherData.value = weatherData
    }
}