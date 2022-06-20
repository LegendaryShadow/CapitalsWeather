package com.tkadela.capitalsweather.list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tkadela.capitalsweather.R
import com.tkadela.capitalsweather.databinding.FragmentWeatherListBinding
import com.tkadela.capitalsweather.network.NetworkLocationInfo

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
        viewModel.navigateToForecastDetail.observe(viewLifecycleOwner) { weatherData ->
            if (weatherData != null) {
                this.findNavController().navigate(
                    WeatherListFragmentDirections.actionWeatherListFragmentToForecastDetailFragment(weatherData))
                viewModel.displayForecastDetailsComplete()
            }
        }

        /**
         * Set observer for network error LiveData
         */
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) {
                onNetworkError()
            }
        }

        /**
         * Observer on the location list to make sure the database is only initialized once
         */
        viewModel.locationList.observe(viewLifecycleOwner) {
            if (viewModel.isWeatherDataInitialized.value == false && it.isNotEmpty()) {
                viewModel.initializeWeatherData()

                with(prefs.edit()) {
                    putBoolean("loc_initialized", true)
                    apply()
                }
            }
        }

        /**
         * Observer to show dialog for multiple location results from search
         */
        viewModel.locationsToSelectFrom.observe(viewLifecycleOwner) {
            if (it != null) {
                showSelectionDialog(it)
                viewModel.locationsDialogComplete()
            }
        }

        // Set listener for SwipeRefreshLayout
        binding.swiper.setOnRefreshListener {
            viewModel.swipeRefresh(binding.swiper)
        }

        activity?.title = "Current Weather"
        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_weather_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_item) {
            showSearchDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Shows dialog when sole options menu item is selected
     */
    private fun showSearchDialog() {
        val editText = EditText(context)
        editText.gravity = Gravity.CENTER_HORIZONTAL

        val dialog = AlertDialog.Builder(context)
            .setTitle("Search for location by name")
            .setMessage("Enter a new location for weather data")
            .setView(editText)
            .setPositiveButton("Search") { _, _ ->
                viewModel.getLocationsFromSearch(editText.text.toString())
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    /**
     * Shows dialog when multiple options are returned from a search
     */
    private fun showSelectionDialog(locList: List<NetworkLocationInfo>) {

        // Convert NetworkLocationInfo to Strings
        val arrayAdapter = ArrayAdapter<String>(context!!, android.R.layout.select_dialog_singlechoice)
        locList.forEach {
            val stringRep: String
            if (it.stateCode == "") {
                stringRep = "${it.city}, ${it.state}, ${it.country.uppercase()}"
            }
            else {
                stringRep = "${it.city}, ${it.stateCode}, ${it.country.uppercase()}"
            }
            arrayAdapter.add(stringRep)
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("Multiple locations found")
            .setAdapter(arrayAdapter) { _, which ->
                viewModel.addSelectedLocation(locList[which])
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
}