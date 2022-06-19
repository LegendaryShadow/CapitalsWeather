package com.tkadela.capitalsweather.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tkadela.capitalsweather.R
import com.tkadela.capitalsweather.domain.CurrentWeather
import com.tkadela.capitalsweather.domain.DayForecast
import com.tkadela.capitalsweather.domain.LocationInfo
import com.tkadela.capitalsweather.domain.WeatherData
import com.tkadela.capitalsweather.network.WeatherApi
import com.tkadela.capitalsweather.network.asDomainModel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class WeatherListViewModel(app: Application) : AndroidViewModel(app) {

    private val _weatherData = MutableLiveData<List<WeatherData>>()
    val weatherData: LiveData<List<WeatherData>>
        get() = _weatherData

    private val _navigateToForecastDetail = MutableLiveData<WeatherData>()
    val navigateToForecastDetail: LiveData<WeatherData>
        get() = _navigateToForecastDetail

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var defaultLocationList = listOf<LocationInfo>()

    init {
        getLocationInfo(app)

        getWeatherDataFromNetwork()
    }

    private fun getLocationInfo(app: Application) {
        val inputStream =
            app.applicationContext.resources.openRawResource(R.raw.state_capital_locations)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val sb = StringBuilder()
        var line = reader.readLine()

        while (line != null) {
            sb.append(line)
            line = reader.readLine()
        }
        reader.close()
        inputStream.close()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(List::class.java, LocationInfo::class.java)
        val adapter: JsonAdapter<List<LocationInfo>> = moshi.adapter(listType)

        defaultLocationList = adapter.fromJson(sb.toString())!!
    }

    private fun getWeatherDataFromNetwork() {
        viewModelScope.launch {
            try {
                val weatherList = defaultLocationList.map() { locationInfo ->

                    val networkWeather =
                        WeatherApi.retrofitService.getWeatherData(
                            locationInfo.lat,
                            locationInfo.lon
                        )

                    networkWeather.asDomainModel(locationInfo.city, locationInfo.state)
                }
                _weatherData.postValue(weatherList)

                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                _eventNetworkError.value = true
            }
        }
    }

    fun displayForecastDetails(weatherData: WeatherData) {
        _navigateToForecastDetail.value = weatherData
    }

    fun displayForecastDetailsComplete() {
        _navigateToForecastDetail.value = null
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }
}