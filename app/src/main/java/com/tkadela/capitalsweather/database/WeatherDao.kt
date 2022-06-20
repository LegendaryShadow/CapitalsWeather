package com.tkadela.capitalsweather.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Class defining the possible queries on the weather database
 */
@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_data_table ORDER BY display_order")
    fun getAllWeather(): LiveData<List<DatabaseWeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(weatherList: List<DatabaseWeatherData>)

    @Query("SELECT * FROM weather_data_table WHERE lat = :lat AND lon = :lon")
    fun getWeather(lat: Double, lon: Double): LiveData<DatabaseWeatherData>

    @Query("UPDATE weather_data_table SET display_order = display_order + 1")
    fun prepForInsert()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: DatabaseWeatherData)
}