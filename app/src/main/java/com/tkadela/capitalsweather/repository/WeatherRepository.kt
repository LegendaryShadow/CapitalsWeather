package com.tkadela.capitalsweather.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tkadela.capitalsweather.R
import com.tkadela.capitalsweather.database.LocationDatabase
import com.tkadela.capitalsweather.database.WeatherDatabase
import com.tkadela.capitalsweather.database.asDomainModel
import com.tkadela.capitalsweather.domain.LocationInfo
import com.tkadela.capitalsweather.domain.WeatherData
import com.tkadela.capitalsweather.domain.asDatabaseModel
import com.tkadela.capitalsweather.network.WeatherApi
import com.tkadela.capitalsweather.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 * This class combines the database and the network data sources
 *
 * The database is updated from the network, and then the UI accesses the weather
 * data from the database
 */
class WeatherRepository(
    private val weatherDatabase: WeatherDatabase,
    private val locationDatabase: LocationDatabase
) {

    /**
     * Access weather data from database as LiveData
     */
    val weatherList: LiveData<List<WeatherData>> =
        Transformations.map(weatherDatabase.weatherDao.getAllWeather()) {
            it.asDomainModel()
        }

    /**
     * Access location data from database as LiveData
     */
    val locationList: LiveData<List<LocationInfo>> =
        Transformations.map(locationDatabase.locationDao.getAllLocations()) {
            it.asDomainModel()
        }

    /**
     * Update weather database from network
     */
    suspend fun refreshWeatherData(locList: List<LocationInfo>) {
        withContext(Dispatchers.IO) {
            val weatherList = locList.map() { locationInfo ->

                val networkWeather =
                    WeatherApi.retrofitService.getWeatherData(
                        locationInfo.lat,
                        locationInfo.lon
                    )

                networkWeather.asDatabaseModel(
                    locationInfo.order,
                    locationInfo.city,
                    locationInfo.state,
                    locationInfo.country
                )
            }

            weatherDatabase.weatherDao.insertAll(weatherList)
        }
    }

    /**
     * Get one location's weather data
     */
    fun getWeatherData(lat: Double, lon: Double) = Transformations.map(weatherDatabase.weatherDao.getWeather(lat, lon)) {
        it.asDomainModel()
    }

    /**
     * Initialize location database from raw resource "state_capital_locations.json"
     */
    suspend fun initializeLocationData(app: Application) {
        withContext(Dispatchers.IO) {
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

            val locList = adapter.fromJson(sb.toString())!!

            locationDatabase.locationDao.insertAll(locList.asDatabaseModel())
        }

    }
}