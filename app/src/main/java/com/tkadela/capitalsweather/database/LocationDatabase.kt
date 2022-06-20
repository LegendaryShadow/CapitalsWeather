package com.tkadela.capitalsweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Singleton database class for the location database
 */
@Database(entities = [DatabaseLocationInfo::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {

    abstract val locationDao: LocationDao

    companion object {

        // The singleton instance. Volatile to make sure all threads access it via main
        // memory with no caching
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        /**
         * Thread-safe method to initialize and access the singleton database
         */
        fun getInstance(context: Context) : LocationDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocationDatabase::class.java,
                        "location_db"
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