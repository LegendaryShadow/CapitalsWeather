package com.tkadela.capitalsweather.domain

data class CurrentWeather(val currentTemp: Int,
                          val feelsLike: Int,
                          val hiTemp: Int,
                          val loTemp: Int,
                          val precipChance: Int)

data class DayForecast(val forecastTime: Int,
                       val weatherType: String,
                       val weatherTypeIcon: String,
                       val hiTemp: Int,
                       val loTemp: Int,
                       val precipChance: Int)

data class WeatherData(val locationLat: Double,
                       val locationLon: Double,
                       val locationCity: String,
                       val locationState: String,
                       val updateTime: Int,
                       val current: CurrentWeather,
                       val dailyForecast: List<DayForecast>)