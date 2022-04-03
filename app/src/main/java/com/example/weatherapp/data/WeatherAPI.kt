package com.example.weatherapp.data

import com.example.weatherapp.data.weather.WeatherAPIResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather?q=Sopot,pl&APPID=b31c2399a91bd7f2a5019bbe3fa1e049&lang=pl")
    fun get(@Query("units") unit: String): Call<WeatherAPIResponse>

    @GET("weather?APPID=b31c2399a91bd7f2a5019bbe3fa1e049&lang=pl")
    fun getByCity(@Query("units") unit: String,
                  @Query("q") city: String): Call<WeatherAPIResponse>


    @GET("weather?APPID=b31c2399a91bd7f2a5019bbe3fa1e049&lang=pl")
    fun getByLocation(@Query("units") unit: String,
                      @Query("lat") latitude: String,
                      @Query("lon") longitude: String): Call<WeatherAPIResponse>

}