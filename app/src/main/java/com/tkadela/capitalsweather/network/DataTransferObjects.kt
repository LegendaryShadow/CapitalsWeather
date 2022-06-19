package com.tkadela.capitalsweather.network

import com.squareup.moshi.Json
import com.tkadela.capitalsweather.domain.CurrentWeather
import com.tkadela.capitalsweather.domain.DayForecast
import com.tkadela.capitalsweather.domain.WeatherData
import java.util.*


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


fun NetworkWeatherData.asDomainModel(city: String, state: String): WeatherData {
    // Get current time zone offset
    val calendar = Calendar.getInstance(Locale.getDefault())
    val tzOffset = -(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000)

    val currentWeather = CurrentWeather(
        current.temp.toInt(),
        current.feelsLike.toInt(),
        daily[0].temp.hi.toInt(),
        daily[0].temp.lo.toInt(),
        (daily[0].precipChance * 100).toInt())

    val dailyForecast = daily.map {
        DayForecast(
            it.forecastTime + locationTzOffset,
            it.weather[0].type,
            it.weather[0].icon,
            it.temp.hi.toInt(),
            it.temp.lo.toInt(),
            (it.precipChance * 100).toInt())
    } .subList(0,5)

    return WeatherData(
        lat,
        lon,
        city,
        state,
        current.updateTime + tzOffset,
        currentWeather,
        dailyForecast)
}