package com.tkadela.capitalsweather.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/data/3.0/"
private const val API_KEY = "709db142b3c9c0768c9299a770527fa1"

// Moshi parser for incoming JSON response
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Retrofit object for weather API access
 *
 * Includes OkHttpClient as interceptor to include query parameters for
 * API key and imperial units (for Fahrenheit temperatures)
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
                    .addQueryParameter("appid", API_KEY)
                    .addQueryParameter("units", "imperial")
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())
            }
            .build()
    )
    .build()

/**
 * Service interface with one endpoint call
 */
interface WeatherApiService {
    @GET("onecall")
    suspend fun getWeatherData(@Query("lat") lat: Double, @Query("lon") lon: Double): NetworkWeatherData
}

/**
 * The access point for the Retrofit API
 *
 * Get weather data from API with
 * WeatherApi.retrofitService.getWeatherData(...)
 */
object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}