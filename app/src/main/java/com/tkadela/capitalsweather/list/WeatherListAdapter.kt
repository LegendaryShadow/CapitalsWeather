package com.tkadela.capitalsweather.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkadela.capitalsweather.databinding.WeatherListItemBinding
import com.tkadela.capitalsweather.domain.WeatherData

/**
 * RecyclerView adapter for current weather list
 *
 * Passes click listener through to the view holder
 */
class WeatherListAdapter(private val onClickListener: WeatherClickListener)
    : ListAdapter<WeatherData, WeatherListAdapter.WeatherViewHolder>(WeatherListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = getItem(position) as WeatherData
        holder.bind(weatherData, onClickListener)
    }

    /**
     * RecyclerView ViewHolder for current weather items
     *
     * Views are filled in with data binding
     */
    class WeatherViewHolder private constructor(val binding: WeatherListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherData, clickListener: WeatherClickListener) {
            binding.weather = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : WeatherViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WeatherListItemBinding.inflate(layoutInflater, parent, false)
                return WeatherViewHolder(binding)
            }
        }
    }
}

/**
 * Class used to tell which parts of the current weather data has been updated
 */
class WeatherListDiffCallback : DiffUtil.ItemCallback<WeatherData>() {

    // Lat/Long is used to identify data items, as they are the primary keys in the database version
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.locationLat == newItem.locationLat &&
                oldItem.locationLon == newItem.locationLon
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem == newItem
    }
}

/**
 * Click listener for RecyclerView items
 */
class WeatherClickListener(val clickListener: (weatherData: WeatherData) -> Unit) {
    fun onClick(weatherData: WeatherData) = clickListener(weatherData)
}