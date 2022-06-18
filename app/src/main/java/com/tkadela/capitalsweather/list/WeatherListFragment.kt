package com.tkadela.capitalsweather.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tkadela.capitalsweather.databinding.FragmentWeatherListBinding

class WeatherListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentWeatherListBinding.inflate(inflater)

        val viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.weatherList.adapter = WeatherListAdapter(WeatherClickListener { weatherData ->
            viewModel.displayForecastDetails(weatherData)
        })

        viewModel.navigateToForecastDetail.observe(viewLifecycleOwner, Observer { weatherData ->
            if (weatherData != null) {
                this.findNavController().navigate(
                    WeatherListFragmentDirections.actionWeatherListFragmentToForecastDetailFragment(weatherData))
                viewModel.displayForecastDetailsComplete()
            }
        })

        activity?.title = "Current Weather"

        return binding.root
    }

}