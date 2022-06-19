package com.tkadela.capitalsweather.network

import com.squareup.moshi.Json
import com.tkadela.capitalsweather.database.DatabaseWeatherData
import com.tkadela.capitalsweather.domain.CurrentWeather
import com.tkadela.capitalsweather.domain.DayForecast
import com.tkadela.capitalsweather.domain.WeatherData
import java.util.*

/**
 * This file contains the objects used to parse the response from the API
 */


data class NetworkWeatherType(
    @Json(name = "main") val type: String,
    val icon: String
)


data class NetworkHiLoTemp(
    @Json(name = "max") val hi: Double,
    @Json(name = "min") val lo: Double
)


data class NetworkDayForecast(
    @Json(name = "dt") val forecastTime: Int,
    val temp: NetworkHiLoTemp,
    val weather: List<NetworkWeatherType>,
    @Json(name = "pop") val precipChance: Double
)


data class NetworkCurrentWeather(
    @Json(name = "dt") val updateTime: Int,
    val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double
)


data class NetworkWeatherData(
    val lat: Double,
    val lon: Double,
    @Json(name = "timezone_offset") val locationTzOffset: Int,
    val current: NetworkCurrentWeather,
    val daily: List<NetworkDayForecast>
)

/**
 * Convert the network weather data to the domain model
 *
 * Requires city and state as parameters as they are not provided by the API response
 */
fun NetworkWeatherData.asDomainModel(city: String, state: String): WeatherData {
    // Get current time zone offset
    val calendar = Calendar.getInstance(Locale.getDefault())
    val tzOffset = -(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000)

    val currentWeather = CurrentWeather(
        current.temp.toInt(),
        current.feelsLike.toInt(),
        daily[0].temp.hi.toInt(),
        daily[0].temp.lo.toInt(),
        (daily[0].precipChance * 100).toInt()
    )

    val dailyForecast = daily.map {
        DayForecast(
            it.forecastTime + locationTzOffset,
            it.weather[0].type,
            it.weather[0].icon,
            it.temp.hi.toInt(),
            it.temp.lo.toInt(),
            (it.precipChance * 100).toInt()
        )
    } .subList(0,5)

    return WeatherData(
        lat,
        lon,
        city,
        state,
        current.updateTime + tzOffset,
        currentWeather,
        dailyForecast
    )
}

/**
 * Convert the network weather data to the database model
 *
 * Requires city and state as parameters as they are not provided by the API response
 */
fun NetworkWeatherData.asDatabaseModel(city: String, state: String): DatabaseWeatherData {
    // Get current time zone offset
    val calendar = Calendar.getInstance(Locale.getDefault())
    val tzOffset = -(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000)

    return DatabaseWeatherData(
        lat,
        lon,
        city,
        state,
        current.updateTime + tzOffset,

        current.temp.toInt(),
        current.feelsLike.toInt(),
        daily[0].temp.hi.toInt(),
        daily[0].temp.lo.toInt(),
        (daily[0].precipChance * 100).toInt(),

        daily[0].forecastTime + locationTzOffset,
        daily[0].weather[0].type,
        daily[0].weather[0].icon,
        daily[0].temp.hi.toInt(),
        daily[0].temp.lo.toInt(),
        (daily[0].precipChance * 100).toInt(),

        daily[1].forecastTime + locationTzOffset,
        daily[1].weather[0].type,
        daily[1].weather[0].icon,
        daily[1].temp.hi.toInt(),
        daily[1].temp.lo.toInt(),
        (daily[1].precipChance * 100).toInt(),

        daily[2].forecastTime + locationTzOffset,
        daily[2].weather[0].type,
        daily[2].weather[0].icon,
        daily[2].temp.hi.toInt(),
        daily[2].temp.lo.toInt(),
        (daily[2].precipChance * 100).toInt(),

        daily[3].forecastTime + locationTzOffset,
        daily[3].weather[0].type,
        daily[3].weather[0].icon,
        daily[3].temp.hi.toInt(),
        daily[3].temp.lo.toInt(),
        (daily[3].precipChance * 100).toInt(),

        daily[4].forecastTime + locationTzOffset,
        daily[4].weather[0].type,
        daily[4].weather[0].icon,
        daily[4].temp.hi.toInt(),
        daily[4].temp.lo.toInt(),
        (daily[4].precipChance * 100).toInt()
    )
}