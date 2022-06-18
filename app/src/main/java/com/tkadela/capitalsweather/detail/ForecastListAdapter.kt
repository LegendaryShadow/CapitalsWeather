package com.tkadela.capitalsweather.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkadela.capitalsweather.databinding.ForecastDayListItemBinding
import com.tkadela.capitalsweather.domain.DayForecast

class ForecastListAdapter
    : ListAdapter<DayForecast, ForecastListAdapter.ForecastDayViewHolder>(ForecastListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastDayViewHolder {
        return ForecastDayViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ForecastDayViewHolder, position: Int) {
        val forecastDay = getItem(position) as DayForecast
        holder.bind(forecastDay)
    }

    class ForecastDayViewHolder private constructor(val binding: ForecastDayListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DayForecast) {
            binding.forecastDay = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ForecastDayViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ForecastDayListItemBinding.inflate(layoutInflater, parent, false)
                return ForecastDayViewHolder(binding)
            }
        }
    }
}


class ForecastListDiffCallback : DiffUtil.ItemCallback<DayForecast>() {

    override fun areItemsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
        return oldItem == newItem
    }
}