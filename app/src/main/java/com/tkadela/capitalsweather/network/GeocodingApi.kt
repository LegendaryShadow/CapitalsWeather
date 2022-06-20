package com.tkadela.capitalsweather.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.geoapify.com/v1/geocode/"
private const val API_KEY = "9dd06c7fc52a42b0b72bee76fb1258a1"

// Moshi parser for incoming JSON response
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Retrofit object for weather API access
 *
 * Includes OkHttpClient as interceptor to include query parameters for
 * API key, format(JSON), and type (city results only)
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val url = chain
                    .request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("apiKey", API_KEY)
                    .addQueryParameter("type", "city")
                    .addQueryParameter("format", "json")
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())
            }
            .build()
    )
    .build()

/**
 * Service interface with one endpoint call
 */
interface GeocodingApiService {
    @GET("search")
    suspend fun getLocationInfo(@Query("text") searchText: String): NetworkLocationContainer
}

/**
 * The access point for the Retrofit API
 *
 * Get weather data from API with
 * WeatherApi.retrofitService.getWeatherData(...)
 */
object GeocodingApi {
    val retrofitService: GeocodingApiService by lazy {
        retrofit.create(GeocodingApiService::class.java)
    }
}