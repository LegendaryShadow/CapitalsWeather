package com.tkadela.capitalsweather.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tkadela.capitalsweather.databinding.FragmentForecastDetailBinding

class ForecastDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentForecastDetailBinding.inflate(inflater)

        // Get parameter from navigation
        val weatherData = ForecastDetailFragmentArgs.fromBundle(requireArguments()).weatherData

        // Initialize ViewModel from factory
        val viewModelFactory = ForecastDetailViewModelFactory(weatherData)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(ForecastDetailViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner

        // Initialize adapter for RecyclerView
        binding.forecastList.adapter = ForecastListAdapter()

        activity?.title = "5-Day Forecast"

        return binding.root
    }

}