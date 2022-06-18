package com.tkadela.capitalsweather.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkadela.capitalsweather.databinding.WeatherListItemBinding
import com.tkadela.capitalsweather.domain.WeatherData

class WeatherListAdapter(private val onClickListener: WeatherClickListener)
    : ListAdapter<WeatherData, WeatherListAdapter.WeatherViewHolder>(WeatherListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = getItem(position) as WeatherData
        holder.bind(weatherData, onClickListener)
    }

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

class WeatherListDiffCallback : DiffUtil.ItemCallback<WeatherData>() {

    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.locationLat == newItem.locationLat &&
                oldItem.locationLon == newItem.locationLon
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem == newItem
    }
}

class WeatherClickListener(val clickListener: (weatherData: WeatherData) -> Unit) {
    fun onClick(weatherData: WeatherData) = clickListener(weatherData)
}