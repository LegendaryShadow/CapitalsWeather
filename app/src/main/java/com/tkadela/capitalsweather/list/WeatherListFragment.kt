package com.tkadela.capitalsweather.list

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

    private val viewModel: WeatherListViewModel by lazy {
        val application = requireNotNull(activity).application
        val viewModelFactory = WeatherListViewModelFactory(application)

        ViewModelProvider(this, viewModelFactory).get(WeatherListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //val application = requireNotNull(activity).application

        val binding = FragmentWeatherListBinding.inflate(inflater)

        //val viewModelFactory = WeatherListViewModelFactory(application)
        //val viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherListViewModel::class.java)

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

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer { isNetworkError ->
            if (isNetworkError) {
                onNetworkError()
            }
        })

        activity?.title = "Current Weather"

        return binding.root
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}