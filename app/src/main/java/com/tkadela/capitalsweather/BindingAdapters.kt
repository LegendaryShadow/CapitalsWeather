package com.tkadela.capitalsweather

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tkadela.capitalsweather.detail.ForecastListAdapter
import com.tkadela.capitalsweather.domain.DayForecast
import com.tkadela.capitalsweather.domain.WeatherData
import com.tkadela.capitalsweather.list.WeatherListAdapter
import java.text.SimpleDateFormat
import java.util.*


/**********************************************************
 * Binding adapters used to format TextViews with simple Strings
 */
@BindingAdapter("city", "state", "country")
fun TextView.setLocationName(city: String, state: String, country: String) {
    var locStr = "${city}, ${state}, ${country}"

    if (locStr.length > 22) {
        locStr = "${locStr.substring(0, 22)}…"
    }

    text = locStr
}

@BindingAdapter("temperature")
fun TextView.setTempFahrenheit(temp: Int) {
    text = "${temp}°F"
}

@BindingAdapter("hiTemp", "loTemp")
fun TextView.setHiLoTempFahrenheit(hiTemp: Int, loTemp: Int) {
    text = "${hiTemp}°F / ${loTemp}°F"
}

@BindingAdapter("feelTemp")
fun TextView.setFeelsLikeTempFahrenheit(temp: Int) {
    text = "Feels Like ${temp}°F"
}

@BindingAdapter("precipChance")
fun TextView.setPrecipChance(precip: Int) {
    text = "${precip}%"
}
/**************************************************************/


/**************************************************************
 * Binding adapters for converting Unix timestamps into various readable formats
 */
@BindingAdapter("dateTime")
fun TextView.setUpdateDateTime(timeStamp: Int) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val dateTime = Date(timeStamp.toLong() * 1000)

    text = "Updated ${sdf.format(dateTime)}"
}

@BindingAdapter("dayOfWeek")
fun TextView.setDayOfWeek(timeStamp: Int) {
    val sdf = SimpleDateFormat("EEEE")
    val dateTime = Date(timeStamp.toLong() * 1000)

    text = sdf.format(dateTime)
}

@BindingAdapter("monthDay")
fun TextView.setMonthDay(timeStamp: Int) {
    val sdf = SimpleDateFormat("MMM dd")
    val dateTime = Date(timeStamp.toLong() * 1000)

    text = sdf.format(dateTime)
}
/*****************************************************************/

/**
 * Binding adapter for reading in weather icons using Glide
 */
@BindingAdapter("weatherImage")
fun ImageView.setWeatherImage(imgCode: String) {
    val url = "http://openweathermap.org/img/wn/${imgCode}@2x.png"
    val imgUri = url.toUri().buildUpon().scheme("https").build()

    Glide.with(context)
        .load(imgUri)
        .apply(RequestOptions()
            .error(R.drawable.ic_connection_error))
        .into(this)
}

/******************************************************************
 * Binding adapters for updating the data in the RecyclerViews
 */
@BindingAdapter("weatherListData")
fun RecyclerView.bindNewWeatherList(data: List<WeatherData>?) {
    if (data != null) {
        val adapter = this.adapter as WeatherListAdapter
        adapter.submitList(data) {
            // Scroll to top after DiffUtil calculates changes
            scrollToPosition(0)
        }
    }
}

@BindingAdapter("forecastListData")
fun RecyclerView.bindNewForecastList(data: List<DayForecast>?) {
    if (data != null) {
        val adapter = this.adapter as ForecastListAdapter
        adapter.submitList(data)
    }
}
/*******************************************************************/

/**
 * Binding adapter for hiding the ProgressBar after loading of the data is complete (successful or not)
 */
@BindingAdapter("isNetworkError", "weatherList")
fun ProgressBar.hideIfNetworkError(isNetworkError: Boolean, weatherList: List<WeatherData>?) {
    visibility = if (weatherList == null || weatherList.isEmpty()) View.VISIBLE else View.GONE

    if (isNetworkError) {
        visibility = View.GONE
    }
}

/**
 * Binding adapter to show ProgressBar while searched location is loading
 */
@BindingAdapter("isLoaded")
fun ProgressBar.showUntilLoaded(isLoaded: Boolean) {
    visibility = if (isLoaded) View.GONE else View.VISIBLE
}