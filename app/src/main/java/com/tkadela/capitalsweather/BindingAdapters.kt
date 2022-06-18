package com.tkadela.capitalsweather

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkadela.capitalsweather.domain.WeatherData
import com.tkadela.capitalsweather.list.WeatherListAdapter

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

// TODO: DateTime string binding adapter

@BindingAdapter("listData")
fun RecyclerView.bindNewList(data: List<WeatherData>) {
    val adapter = this.adapter as WeatherListAdapter
    adapter.submitList(data)
}