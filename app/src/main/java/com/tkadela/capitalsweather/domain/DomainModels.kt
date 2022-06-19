package com.tkadela.capitalsweather.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * This file contains the domain models for weather data. These are the objects
 * that will be displayed on the screen.
 *
 * @see database package for the corresponding objects that are mapped to the database
 * @see network package for the corresponding objects that parse network calls
 */


/**
 * Data class for the current weather details
 */
@Parcelize
data class CurrentWeather(val currentTemp: Int,
                          val feelsLike: Int,
                          val hiTemp: Int,
                          val loTemp: Int,
                          val precipChance: Int) : Parcelable   // precipitation chance as integer percentage

/**
 * Data class for a single day's forecast
 */
@Parcelize
data class DayForecast(val forecastTime: Int,   // time as Unix timestamp (in location time zone)
                       val weatherType: String, // Clear, Clouds, Rain, etc.
                       val weatherTypeIcon: String,     // The unique part of the URL for the weather image
                       val hiTemp: Int,
                       val loTemp: Int,
                       val precipChance: Int) : Parcelable    // precipitation chance as integer percentage

/**
 * Data class to combine the current weather and 5-day forecast,
 * along with the location data
 */
@Parcelize
data class WeatherData(val locationLat: Double,
                       val locationLon: Double,
                       val locationCity: String,
                       val locationState: String,
                       val updateTime: Int,     // time as Unix timestamp (in user time zone)
                       val current: CurrentWeather,
                       val dailyForecast: List<DayForecast>) : Parcelable