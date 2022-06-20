package com.tkadela.capitalsweather.network

import com.squareup.moshi.Json
import com.tkadela.capitalsweather.database.DatabaseLocationInfo

data class NetworkLocationContainer(
    val results: List<NetworkLocationInfo>
)

data class NetworkLocationInfo(
    val state: String,
    @Json(name = "state_code") val stateCode: String = "",
    val city: String,
    @Json(name = "country_code") val country: String,
    val lat: Double,
    val lon : Double
)

fun NetworkLocationInfo.asDatabaseModel(): DatabaseLocationInfo {

    val networkState = if (stateCode == "")  state else stateCode

    return DatabaseLocationInfo(1, networkState, city, country.uppercase(), lat, lon)
}