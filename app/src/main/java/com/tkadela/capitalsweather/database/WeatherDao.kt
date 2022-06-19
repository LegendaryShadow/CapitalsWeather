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

    @Query("SELECT * FROM weather_data_table")
    fun getAllWeather(): LiveData<List<DatabaseWeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(weatherList: List<DatabaseWeatherData>)
}