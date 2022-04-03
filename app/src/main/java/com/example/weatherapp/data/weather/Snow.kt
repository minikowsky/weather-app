package com.example.weatherapp.data.weather

import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName(value = "1h")
    val last1h: Double,
    @SerializedName(value = "3h")
    val last3h: Double
)
