package com.tkadela.capitalsweather.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationInfo(val state: String,
                        val city: String,
                        val lat: Double,
                        val lon : Double)