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

@BindingAdapter("city", "state")
fun TextView.setCityState(city: String, state: String) {
    text = "${city}, ${state}"
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

@BindingAdapter("weatherListData")
fun RecyclerView.bindNewWeatherList(data: List<WeatherData>?) {
    if (data != null) {
        val adapter = this.adapter as WeatherListAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("isNetworkError", "weatherList")
fun ProgressBar.hideIfNetworkError(isNetworkError: Boolean, weatherList: List<WeatherData>?) {
    visibility = if (weatherList == null || weatherList.isEmpty()) View.VISIBLE else View.GONE

    if (isNetworkError) {
        visibility = View.GONE
    }
}

@BindingAdapter("forecastListData")
fun RecyclerView.bindNewForecastList(data: List<DayForecast>?) {
    if (data != null) {
        val adapter = this.adapter as ForecastListAdapter
        adapter.submitList(data)
    }
}