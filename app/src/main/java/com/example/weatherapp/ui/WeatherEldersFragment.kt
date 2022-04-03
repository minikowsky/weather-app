package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.Unit
import com.example.weatherapp.viewmodels.WeatherViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class WeatherEldersFragment : Fragment() {

    lateinit var  viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
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

        val sharedPref = (activity as MainActivity).applicationContext.getSharedPreferences(
            "UserPreferences",
            Context.MODE_PRIVATE
        )


        viewModel.weather.observe(viewLifecycleOwner) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
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

        sharedPref.getString("Unit", Unit.METRIC)?.let { viewModel.getWeather(it) }

        view.findViewById<ImageButton>(R.id.button_set_city).setOnClickListener{
            val city: String = view.findViewById<EditText>(R.id.edit_text_city).text.toString()
            if(city.isNotBlank())
                sharedPref.getString("Unit", Unit.METRIC)
                    ?.let { it1 -> viewModel.getWeatherByCity(it1,city) }
        }

    }
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