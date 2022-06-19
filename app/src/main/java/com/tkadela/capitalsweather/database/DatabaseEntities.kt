package com.tkadela.capitalsweather.database

import androidx.room.Entity
import com.tkadela.capitalsweather.domain.CurrentWeather
import com.tkadela.capitalsweather.domain.DayForecast
import com.tkadela.capitalsweather.domain.WeatherData

/**
 * This class defines the database schema for the weather data table.
 * It is defined as one large table for simplicity of access.
 */
@Entity(tableName = "weather_data_table", primaryKeys = ["lat", "lon"])
data class DatabaseWeatherData(
    val lat: Double,
    val lon: Double,
    val city: String,
    val state: String,
    val updateTime: Int,

    val currentTemp: Int,
    val currentFeelsLike: Int,
    val currentHiTemp: Int,
    val currentLoTemp: Int,
    val currentPrecipChance: Int,

    val day1ForecastTime: Int,
    val day1WeatherType: String,
    val day1WeatherTypeIcon: String,
    val day1HiTemp: Int,
    val day1LoTemp: Int,
    val day1PrecipChance: Int,

    val day2ForecastTime: Int,
    val day2WeatherType: String,
    val day2WeatherTypeIcon: String,
    val day2HiTemp: Int,
    val day2LoTemp: Int,
    val day2PrecipChance: Int,

    val day3ForecastTime: Int,
    val day3WeatherType: String,
    val day3WeatherTypeIcon: String,
    val day3HiTemp: Int,
    val day3LoTemp: Int,
    val day3PrecipChance: Int,

    val day4ForecastTime: Int,
    val day4WeatherType: String,
    val day4WeatherTypeIcon: String,
    val day4HiTemp: Int,
    val day4LoTemp: Int,
    val day4PrecipChance: Int,

    val day5ForecastTime: Int,
    val day5WeatherType: String,
    val day5WeatherTypeIcon: String,
    val day5HiTemp: Int,
    val day5LoTemp: Int,
    val day5PrecipChance: Int
)

/**
 * Convert the database weather data to the domain model
 */
fun List<DatabaseWeatherData>.asDomainModel(): List<WeatherData> {
    return map {
        val current = CurrentWeather(it.currentTemp, it.currentFeelsLike, it.currentHiTemp, it.currentLoTemp, it.currentPrecipChance)

        val day1Forecast = DayForecast(it.day1ForecastTime, it.day1WeatherType, it.day1WeatherTypeIcon, it.day1HiTemp, it.day1LoTemp, it.day1PrecipChance)
        val day2Forecast = DayForecast(it.day2ForecastTime, it.day2WeatherType, it.day2WeatherTypeIcon, it.day2HiTemp, it.day2LoTemp, it.day2PrecipChance)
        val day3Forecast = DayForecast(it.day3ForecastTime, it.day3WeatherType, it.day3WeatherTypeIcon, it.day3HiTemp, it.day3LoTemp, it.day3PrecipChance)
        val day4Forecast = DayForecast(it.day4ForecastTime, it.day4WeatherType, it.day4WeatherTypeIcon, it.day4HiTemp, it.day4LoTemp, it.day4PrecipChance)
        val day5Forecast = DayForecast(it.day5ForecastTime, it.day5WeatherType, it.day5WeatherTypeIcon, it.day5HiTemp, it.day5LoTemp, it.day5PrecipChance)
        val dailyForecast = listOf(day1Forecast, day2Forecast, day3Forecast, day4Forecast, day5Forecast)

        WeatherData(it.lat, it.lon, it.city, it.state, it.updateTime, current, dailyForecast)
    }

}