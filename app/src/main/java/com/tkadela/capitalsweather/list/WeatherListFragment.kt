package com.tkadela.capitalsweather.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkadela.capitalsweather.databinding.FragmentWeatherListBinding

class WeatherListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentWeatherListBinding.inflate(inflater)

        activity?.title = "Current Weather"

        return binding.root
    }

}