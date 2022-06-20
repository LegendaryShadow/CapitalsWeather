package com.tkadela.capitalsweather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.tkadela.capitalsweather.domain.LocationInfo

/**
 * This class defines the database schema for the location data table.
 */
@Entity(tableName = "location_data_table", primaryKeys = ["lat", "lon"])
data class DatabaseLocationInfo(
    @ColumnInfo(name = "display_order") val order: Int,
    val state: String,
    val city: String,
    val country: String,
    val lat: Double,
    val lon : Double
)

/**
 * Converts database location data to the domain model
 */
fun List<DatabaseLocationInfo>.asDomainModel(): List<LocationInfo> {
    return map {
        LocationInfo(it.order, it.state, it.city, it.country, it.lat, it.lon)
    }
}