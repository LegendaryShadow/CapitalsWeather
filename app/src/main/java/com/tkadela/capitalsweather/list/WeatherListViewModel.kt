package com.tkadela.capitalsweather.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tkadela.capitalsweather.R
import com.tkadela.capitalsweather.database.WeatherDatabase
import com.tkadela.capitalsweather.domain.LocationInfo
import com.tkadela.capitalsweather.domain.WeatherData
import com.tkadela.capitalsweather.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class WeatherListViewModel(app: Application) : AndroidViewModel(app) {

    private val weatherRepository = WeatherRepository(WeatherDatabase.getInstance(app))

    val weatherData = weatherRepository.weatherList

    private val _navigateToForecastDetail = MutableLiveData<WeatherData>()
    val navigateToForecastDetail: LiveData<WeatherData>
        get() = _navigateToForecastDetail

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var locationList = listOf<LocationInfo>()

    init {
        getLocationInfo(app)

        viewModelScope.launch {
            getWeatherDataFromRepository()
        }
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

        locationList = adapter.fromJson(sb.toString())!!
    }

    private suspend fun getWeatherDataFromRepository() {
        try {
            weatherRepository.refreshWeatherData(locationList)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        } catch (networkError: IOException) {
            if (weatherData.value.isNullOrEmpty()) {
                _eventNetworkError.value = true
            }
        }

    }

    fun swipeRefresh(swiper: SwipeRefreshLayout) {
        viewModelScope.launch {
            getWeatherDataFromRepository()

            swiper.isRefreshing = false
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