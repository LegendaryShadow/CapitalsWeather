package com.tkadela.capitalsweather.domain

import com.squareup.moshi.JsonClass

/**
 * Data class for parsing raw resource "state_capital_locations.json"
 */
@JsonClass(generateAdapter = true)
data class LocationInfo(val state: String,
                        val city: String,
                        val lat: Double,
                        val lon : Double)