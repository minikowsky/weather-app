package com.example.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.Unit
import com.example.weatherapp.data.weather.Main
import com.example.weatherapp.viewmodels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class WeatherFragment : Fragment() {

    lateinit var  viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = (activity as MainActivity).applicationContext.getSharedPreferences(
            "UserPreferences",
            Context.MODE_PRIVATE
        )


        viewModel.weather.observe(viewLifecycleOwner) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val timeFormat = SimpleDateFormat("HH:mm",Locale.ENGLISH)
            view.findViewById<TextView>(R.id.text_view_date)
                .text = dateFormat.format(Date(it.dt * 1000L))
            view.findViewById<TextView>(R.id.text_view_time)
                .text = timeFormat.format(Date(it.dt * 1000L))

            view.findViewById<TextView>(R.id.text_view_description)
                .text = it.weather[0].description

            val unitSymbol: String = when(sharedPref.getString("Unit", Unit.METRIC)) {
                Unit.METRIC -> Unit.Symbol.METRIC
                Unit.IMPERIAL -> Unit.Symbol.IMPERIAL
                else -> Unit.Symbol.STANDARD
            }

            view.findViewById<TextView>(R.id.text_view_temp)
                .text = "${it.main.temp} $unitSymbol"
            view.findViewById<TextView>(R.id.text_view_pressure)
                .text = "${it.main.pressure} hPa"
            view.findViewById<TextView>(R.id.text_view_sunrise)
                .text = timeFormat.format(Date(it.sys.sunrise * 1000L))
            view.findViewById<TextView>(R.id.text_view_sunset)
                .text = timeFormat.format(Date(it.sys.sunset * 1000L))
        }



        view.findViewById<ImageButton>(R.id.button_set_city).setOnClickListener{
            val city: String = view.findViewById<EditText>(R.id.edit_text_city).text.toString()
            if(city.isNotBlank())
                sharedPref.getString("Unit", Unit.METRIC)
                    ?.let { it1 -> viewModel.getWeatherByCity(it1,city) }

        }

        sharedPref.getString("Unit", Unit.METRIC)?.let { viewModel.getWeather(it) }

        requestForLocationPermission()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted:Boolean ->
        if(isGranted) {
            loadWeatherByLocation()
            } else {
                Log.i("Permission", "Denied")
        }
    }

    private fun loadWeatherByLocation() {
        val sharedPref = (activity as MainActivity).applicationContext.getSharedPreferences(
            "UserPreferences",
            Context.MODE_PRIVATE
        )
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                val latitude = location?.latitude
                val longitude = location?.longitude
                println("$latitude $longitude")

                sharedPref.getString("Unit", Unit.METRIC)?.let { viewModel.getWeatherByLocation(it,latitude,longitude) }
            }
        //
    }

    private fun isLocationEnabled(): Boolean {
        return !(ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestForLocationPermission() {
        when {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                println("GRANTED!!!")
                loadWeatherByLocation()
            }
            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weather,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_action_settings) {
            view?.findNavController()?.navigate(R.id.action_weatherFragment_to_settingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }


}