package com.example.weatherapp

object Unit {
    const val METRIC: String = "metric"
    const val IMPERIAL: String = "imperial"
    const val STANDARD: String = "standard"

    object Symbol {

        object Degree {
            const val METRIC: String = "°C"
            const val IMPERIAL: String = "°F"
            const val STANDARD: String = "K"
        }

        object Speed {
            const val METRIC: String = "m/s"
            const val IMPERIAL: String = "m/s"
            const val STANDARD: String = "mil/hour"
        }
    }
}