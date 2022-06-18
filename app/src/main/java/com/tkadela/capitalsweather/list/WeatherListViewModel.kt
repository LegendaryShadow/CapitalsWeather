package com.tkadela.capitalsweather.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tkadela.capitalsweather.domain.CurrentWeather
import com.tkadela.capitalsweather.domain.DayForecast
import com.tkadela.capitalsweather.domain.WeatherData

class WeatherListViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<List<WeatherData>>()
    val weatherData: LiveData<List<WeatherData>>
        get() = _weatherData

    init {
        // TODO: Get data from API
        // DUMMY DATA FOLLOWS
        val acurrent = CurrentWeather(68, 68, 73, 51, 1)
        val aday1Forecast = DayForecast(1655531462, "Clear", "01n", 73, 51, 1)
        val aday2Forecast = DayForecast(1655617862, "Clear", "01d", 74, 53, 1)
        val aday3Forecast = DayForecast(1655704262, "Clear", "01d", 85, 64, 3)
        val aday4Forecast = DayForecast(1655790662, "Clear", "01d", 93, 69, 1)
        val aday5Forecast = DayForecast(1655877062, "Clear", "01d", 98, 75, 18)
        val aweather = WeatherData(41.6186, -87.8805, "Orland Park", "IL", 1655531462, acurrent,
                        listOf(aday1Forecast, aday2Forecast, aday3Forecast, aday4Forecast, aday5Forecast))

        val bcurrent = CurrentWeather(80, 87, 91, 78, 2)
        val bday1Forecast = DayForecast(1655531462, "Clear", "01n", 91, 78, 2)
        val bday2Forecast = DayForecast(1655617862, "Clear", "01d", 92, 78, 22)
        val bday3Forecast = DayForecast(1655704262, "Thunderstorm", "11d", 88, 76, 57)
        val bday4Forecast = DayForecast(1655790662, "Thunderstorm", "11d", 89, 74, 58)
        val bday5Forecast = DayForecast(1655877062, "Thunderstorm", "11d", 90, 73, 58)
        val bweather = WeatherData(26.1420, -87.7948, "Naples", "FL", 1655531462, bcurrent,
            listOf(bday1Forecast, bday2Forecast, bday3Forecast, bday4Forecast, bday5Forecast))

        _weatherData.value = listOf(aweather, bweather)
    }
}