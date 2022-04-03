package com.example.weatherapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.weather.WeatherAPIResponse
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class WeatherViewModel : ViewModel(){
    private val _weather: MutableLiveData<WeatherAPIResponse> = MutableLiveData()
    val weather: LiveData<WeatherAPIResponse>
        get() {
            return _weather
        }

    fun getWeather(unit: String) {
        viewModelScope.launch {
            val w = WeatherRepository.get(unit)
            if(w != null){
                _weather.value = w!!
            }
        }
    }

    fun getWeatherByCity(unit: String, city: String) {
        viewModelScope.launch {
            val w = WeatherRepository.getByCity(unit, city)
            if(w != null) {
                _weather.value = w!!
            }
        }
    }

    fun getWeatherByLocation(unit: String, latitude: Double?, longitude: Double?) {
        if(latitude == null || longitude == null) {
            getWeather(unit)
        }

        viewModelScope.launch {
            val w = WeatherRepository.getByLocation(unit, latitude.toString(), longitude.toString())
            if(w != null) {
                _weather.value = w!!
                Log.i("ApiResponse LOCATION",w.toString())
            }
        }
    }
}