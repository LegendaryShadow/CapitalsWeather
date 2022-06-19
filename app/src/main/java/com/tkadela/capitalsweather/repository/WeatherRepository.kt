package com.tkadela.capitalsweather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tkadela.capitalsweather.database.WeatherDatabase
import com.tkadela.capitalsweather.database.asDomainModel
import com.tkadela.capitalsweather.domain.LocationInfo
import com.tkadela.capitalsweather.domain.WeatherData
import com.tkadela.capitalsweather.network.WeatherApi
import com.tkadela.capitalsweather.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This class combines the database and the network data sources
 *
 * The database is updated from the network, and then the UI accesses the weather
 * data from the database
 */
class WeatherRepository(private val database: WeatherDatabase) {

    /**
     * Access weather data from database as LiveData
     */
    val weatherList: LiveData<List<WeatherData>> = Transformations.map(database.weatherDao.getAllWeather()) {
        it.asDomainModel()
    }

    /**
     * Update database from network
     */
    suspend fun refreshWeatherData(locationList: List<LocationInfo>) {
        withContext(Dispatchers.IO) {

            val weatherList = locationList.map() { locationInfo ->

                val networkWeather =
                    WeatherApi.retrofitService.getWeatherData(
                        locationInfo.lat,
                        locationInfo.lon
                    )

                networkWeather.asDatabaseModel(locationInfo.city, locationInfo.state)
            }

            database.weatherDao.insertAll(weatherList)
        }
    }
}