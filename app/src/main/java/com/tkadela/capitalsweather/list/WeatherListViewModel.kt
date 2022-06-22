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
import com.tkadela.capitalsweather.database.LocationDatabase
import com.tkadela.capitalsweather.database.WeatherDatabase
import com.tkadela.capitalsweather.domain.LocationInfo
import com.tkadela.capitalsweather.domain.WeatherData
import com.tkadela.capitalsweather.network.GeocodingApi
import com.tkadela.capitalsweather.network.NetworkLocationInfo
import com.tkadela.capitalsweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 * ViewModel for the WeatherListFragment
 */
class WeatherListViewModel(app: Application, isLocationInitialized: Boolean) : AndroidViewModel(app) {

    // Get the singleton object of the weather repository
    private val weatherRepository =
        WeatherRepository(WeatherDatabase.getInstance(app), LocationDatabase.getInstance(app))

    /**
     * List of weather data from database (LiveData)
     * This will be the data for the RecyclerView in this Fragment
     */
    val weatherData = weatherRepository.weatherList

    /**
     * List of location data from database (LiveData)
     * This will be used to make calls to the weather API
     */
    val locationList = weatherRepository.locationList

    /**
     * Variable that tells the Fragment to navigate to a specific [ForecastDetailFragment]
     *
     * Mutable version is private to prevent modification outside the ViewModel
     */
    private val _navigateToForecastDetail = MutableLiveData<WeatherData>()
    val navigateToForecastDetail: LiveData<WeatherData>
        get() = _navigateToForecastDetail

    /**
     * Variable that triggers the Fragment to display a Toast for a network error
     */
    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag that marks whether the network error Toast has been shown by the Fragment
     */
    private val _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * Flag that marks whether the weather data has been initialized
     */
    private val _isWeatherDataInitialized = MutableLiveData(false)
    val isWeatherDataInitialized: LiveData<Boolean>
        get() = _isWeatherDataInitialized

    /**
     * Variable that tells the Fragment to show an AlertDialog to select the correct location
     */
    private val _locationsToSelectFrom = MutableLiveData<List<NetworkLocationInfo>>()
    val locationsToSelectFrom: LiveData<List<NetworkLocationInfo>>
        get() = _locationsToSelectFrom

    /**
     * Variable that tells the Fragment to show an AlertDialog to select the correct location
     */
    private val _isNewLocationLoaded = MutableLiveData(true)
    val isNewLocationLoaded: LiveData<Boolean>
        get() = _isNewLocationLoaded

    /**
     * Variable that tells the Fragment to show a Toast that no matches were found for the user's search
     */
    private val _isNoMatchesShown = MutableLiveData(true)
    val isNoMatchesShown: LiveData<Boolean>
        get() = _isNoMatchesShown

    /**
     * Initialize ViewModel by reading the location info from the raw JSON file
     * and then updating the weather database with API calls
     */
    init {
        viewModelScope.launch {
            if (!isLocationInitialized) {
                weatherRepository.initializeLocationData(app)
            }
        }
    }

    /**
     * Initialize weather data after location data is read from database
     */
    fun initializeWeatherData() {
        viewModelScope.launch {
            getWeatherDataFromRepository()

            _isWeatherDataInitialized.value = true
        }
    }

    /**
     * Get weather data from database and execute API calls to update database
     */
    private suspend fun getWeatherDataFromRepository() {
        try {
            weatherRepository.refreshWeatherData(locationList.value!!)

            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false

        } catch (networkError: IOException) {
            if (weatherData.value.isNullOrEmpty()) {
                _eventNetworkError.value = true
            }
        }
    }

    /**
     * Update weather data from API on swipe-down
     */
    fun swipeRefresh(swiper: SwipeRefreshLayout) {
        viewModelScope.launch {
            getWeatherDataFromRepository()

            swiper.isRefreshing = false
        }
    }

    /**
     * Initiate navigation to [ForecastDetailFragment] for given location
     */
    fun displayForecastDetails(weatherData: WeatherData) {
        _navigateToForecastDetail.value = weatherData
    }

    /**
     * Reset LiveData for navigation
     */
    fun displayForecastDetailsComplete() {
        _navigateToForecastDetail.value = null
    }

    /**
     * Reset LiveData for showing network error
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    /**
     * Make Geocoding API call to search for a new location for weather data
     */
    fun getLocationsFromSearch(searchText: String) {
        viewModelScope.launch {
            val networkLocContainer = GeocodingApi.retrofitService.getLocationInfo(searchText)
            val networkLocList = networkLocContainer.results

            if (networkLocList.size == 1) {
                // Add directly if only one result
                weatherRepository.addNewLocationAndWeather(networkLocList[0])
            }
            else if (networkLocList.size == 0) {
                _isNoMatchesShown.value = false
            }
            else {
                // Show selection dialog if multiple results
                _locationsToSelectFrom.value = networkLocList
            }
        }
    }

    /**
     * Adds selected location from selection dialog to database
     */
    fun addSelectedLocation(networkLocationInfo: NetworkLocationInfo) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _isNewLocationLoaded.value = false  // Controls for ProgressBar visibility
            }

            weatherRepository.addNewLocationAndWeather(networkLocationInfo)

            withContext(Dispatchers.Main) {
                _isNewLocationLoaded.value = true  // Controls for ProgressBar visibility
            }
        }
    }

    // Reset LiveData for location select dialog
    fun locationsDialogComplete() {
        _locationsToSelectFrom.value = null
    }

    // Reset LiveData for location select dialog
    fun showNoMatchesComplete() {
        _isNoMatchesShown.value = true
    }
}