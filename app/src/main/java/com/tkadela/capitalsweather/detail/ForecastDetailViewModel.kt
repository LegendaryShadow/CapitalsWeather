package com.tkadela.capitalsweather.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tkadela.capitalsweather.domain.WeatherData

class ForecastDetailViewModel(weatherData: WeatherData) : ViewModel() {

    private val _selectedWeatherData = MutableLiveData<WeatherData>()
    val selectedWeatherData: LiveData<WeatherData>
        get() = _selectedWeatherData

    init {
        _selectedWeatherData.value = weatherData
    }
}