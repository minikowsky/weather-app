package com.example.weatherapp.data

import com.example.weatherapp.data.weather.WeatherAPIResponse
import retrofit2.awaitResponse

class WeatherRepository {
    companion object {
        suspend fun get(unit: String, city: String): WeatherAPIResponse? {
            return RetrofitInstance.api.get(unit, city).awaitResponse().body()
        }

        suspend fun getByLocation(unit: String, latitude: String, longitude: String): WeatherAPIResponse? {
            return RetrofitInstance.api.getByLocation(unit,latitude,longitude).awaitResponse().body()
        }
    }
}