package com.tkadela.capitalsweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Singleton database class for the weather database
 */
@Database(entities = [DatabaseWeatherData::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao

    companion object {

        // The singleton instance. Volatile to make sure all threads access it via main
        // memory with no caching
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        /**
         * Thread-safe method to initialize and access the singleton database
         */
        fun getInstance(context: Context) : WeatherDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            WeatherDatabase::class.java,
                            "weather_db"
                        )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}