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
interface LocationDao {

    @Query("SELECT * FROM location_data_table ORDER BY display_order")
    fun getAllLocations(): LiveData<List<DatabaseLocationInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locationList: List<DatabaseLocationInfo>)

    @Query("UPDATE location_data_table SET display_order = display_order + 1")
    fun prepForInsert()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: DatabaseLocationInfo)

}