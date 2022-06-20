package com.tkadela.capitalsweather.domain

import com.squareup.moshi.JsonClass
import com.tkadela.capitalsweather.database.DatabaseLocationInfo

/**
 * Data class for parsing raw resource "state_capital_locations.json"
 */
@JsonClass(generateAdapter = true)
data class LocationInfo(val order: Int,
                        val state: String,
                        val city: String,
                        val country: String,
                        val lat: Double,
                        val lon : Double)

/**
 * Converts domain location data to the database model
 */
fun List<LocationInfo>.asDatabaseModel(): List<DatabaseLocationInfo> {
    return map {
        DatabaseLocationInfo(it.order, it.state, it.city, it.country, it.lat, it.lon)
    }
}