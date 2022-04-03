package com.example.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.tools.Unit
import com.example.weatherapp.tools.WeatherIconTool
import com.example.weatherapp.viewmodels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class WeatherEldersFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var  viewModel: WeatherViewModel

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_elders, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //user preferences, for instance units or application mode (for elders or normal)
        val sharedPref = (activity as MainActivity).applicationContext.getSharedPreferences(
            "UserPreferences",
            Context.MODE_PRIVATE
        )

        //temperature degree symbol depends on unit
        val degree: String = when(sharedPref.getString("Unit", Unit.METRIC)) {
            Unit.METRIC -> Unit.Degree.METRIC
            Unit.IMPERIAL -> Unit.Degree.IMPERIAL
            else -> Unit.Degree.STANDARD
        }

        //setting view components to observe changes in viewmodel
        viewModel.weather.observe(viewLifecycleOwner) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

            //Date
            view.findViewById<TextView>(R.id.text_view_date)
                .text = dateFormat.format(Date(it.dt * 1000L))
            //City
            view.findViewById<TextView>(R.id.text_view_city_name)
                .text = it.name
            //Time
            view.findViewById<TextView>(R.id.text_view_time)
                .text = timeFormat.format(Date(it.dt * 1000L))

            //Image
            view.findViewById<ImageView>(R.id.image_view_icon)
                .setImageResource(WeatherIconTool.getMipmapId(it.weather[0].icon))
            //Description
            view.findViewById<TextView>(R.id.text_view_description)
                .text = it.weather[0].description
            //Temperature
            view.findViewById<TextView>(R.id.text_view_temp)
                .text = "${it.main.temp} $degree"
            //Pressure
            view.findViewById<TextView>(R.id.text_view_pressure)
                .text = "${it.main.pressure} hPa"


            //Sunrise time
            view.findViewById<TextView>(R.id.text_view_sunrise)
                .text = timeFormat.format(Date(it.sys.sunrise * 1000L))
            //Sunset time
            view.findViewById<TextView>(R.id.text_view_sunset)
                .text = timeFormat.format(Date(it.sys.sunset * 1000L))
        }

        view.findViewById<ImageButton>(R.id.button_set_city).setOnClickListener{
            val city: String = view.findViewById<EditText>(R.id.edit_text_city).text.toString()
            if(city.isNotBlank())
                sharedPref.getString("Unit", Unit.METRIC)
                    ?.let { it1 -> viewModel.getWeather(it1,city) }
        }

        //Loading data for default city (Perth)
        sharedPref.getString("Unit", Unit.METRIC)?.let { viewModel.getWeather(it) }

        //Updating data if location permission is granted
        requestForLocationPermission()
    }

    private fun requestForLocationPermission() {
        when {
            // if permission is granted then load weather by location
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadWeatherByLocation()
            }
            //if not, ask for permission
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    //permission launcher that asks for location permisson
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted:Boolean ->
        if(isGranted) {
            //if is granted, then load weather by location
            loadWeatherByLocation()
        }
    }

    private fun loadWeatherByLocation() {
        //get user preferences to get unit
        val sharedPref = (activity as MainActivity).applicationContext.getSharedPreferences(
            "UserPreferences",
            Context.MODE_PRIVATE
        )
        //when location is available, get weather by location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                val latitude = location?.latitude
                val longitude = location?.longitude
                sharedPref.getString("Unit", Unit.METRIC)?.let { viewModel.getWeatherByLocation(it,latitude,longitude) }
            }
    }

    //TOP BAR MENU
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weather,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_action_settings) {
            view?.findNavController()?.navigate(R.id.action_weatherEldersFragment_to_settingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}