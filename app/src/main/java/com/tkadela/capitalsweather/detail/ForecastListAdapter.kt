package com.tkadela.capitalsweather.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkadela.capitalsweather.databinding.ForecastDayListItemBinding
import com.tkadela.capitalsweather.domain.DayForecast

/**
 * RecyclerView adapter for daily forecast list
 */
class ForecastListAdapter
    : ListAdapter<DayForecast, ForecastListAdapter.ForecastDayViewHolder>(ForecastListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastDayViewHolder {
        return ForecastDayViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ForecastDayViewHolder, position: Int) {
        val forecastDay = getItem(position) as DayForecast
        holder.bind(forecastDay)
    }

    /**
     * RecyclerView ViewHolder for forecast day items
     *
     * Views are filled in with data binding
     */
    class ForecastDayViewHolder private constructor(val binding: ForecastDayListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind action for this ViewHolder type
         */
        fun bind(item: DayForecast) {
            binding.forecastDay = item
            binding.executePendingBindings()
        }

        companion object {
            /**
             * Static function to create a ForecastDayViewHolder
             */
            fun from(parent: ViewGroup) : ForecastDayViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ForecastDayListItemBinding.inflate(layoutInflater, parent, false)
                return ForecastDayViewHolder(binding)
            }
        }
    }
}

/**
 * Class used to tell which parts of the forecast data has been updated
 */
class ForecastListDiffCallback : DiffUtil.ItemCallback<DayForecast>() {

    // The data in the items can change, but the items will never move in the list,
    // so items can be identified by address
    override fun areItemsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
        return oldItem == newItem
    }
}