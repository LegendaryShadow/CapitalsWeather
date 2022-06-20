package com.tkadela.capitalsweather.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tkadela.capitalsweather.databinding.FragmentWeatherListBinding

class WeatherListFragment : Fragment() {

    private var isLocationInitialized = false

    /**
     * Initialize ViewModel from factory
     */
    private val viewModel: WeatherListViewModel by lazy {
        val application = requireNotNull(activity).application
        val viewModelFactory = WeatherListViewModelFactory(application, isLocationInitialized)

        ViewModelProvider(this, viewModelFactory).get(WeatherListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentWeatherListBinding.inflate(inflater)

        val prefs = requireNotNull(activity).getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        isLocationInitialized = prefs.getBoolean("loc_initialized", false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // Initialize adapter for RecyclerView
        binding.weatherList.adapter = WeatherListAdapter(WeatherClickListener { weatherData ->
            viewModel.displayForecastDetails(weatherData)
        })

        /**
         * Set observer for [ForecastDetailFragment] navigation LiveData
         */
        viewModel.navigateToForecastDetail.observe(viewLifecycleOwner, Observer { weatherData ->
            if (weatherData != null) {
                this.findNavController().navigate(
                    WeatherListFragmentDirections.actionWeatherListFragmentToForecastDetailFragment(weatherData))
                viewModel.displayForecastDetailsComplete()
            }
        })

        /**
         * Set observer for network error LiveData
         */
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer { isNetworkError ->
            if (isNetworkError) {
                onNetworkError()
            }
        })

        viewModel.locationList.observe(viewLifecycleOwner, Observer {
            if (viewModel.isWeatherDataInitialized.value == false && it.isNotEmpty()) {
                viewModel.initializeWeatherData()

                with(prefs.edit()) {
                    putBoolean("loc_initialized", true)
                    apply()
                }
            }
        })

        // Set listener for SwipeRefreshLayout
        binding.swiper.setOnRefreshListener {
            viewModel.swipeRefresh(binding.swiper)
        }

        activity?.title = "Current Weather"

        return binding.root
    }

    /**
     * Show Toast for network error
     */
    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}