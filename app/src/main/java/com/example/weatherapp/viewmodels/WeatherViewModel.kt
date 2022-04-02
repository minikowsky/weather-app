package com.example.weatherapp.viewmodels

import androidx.lifecycle.*
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

}