package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.ui.WeatherEldersFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = applicationContext.getSharedPreferences("UserPreferences", MODE_PRIVATE)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController


        if (sharedPref.getBoolean("ElderMode", false)) {
            if(navController.currentDestination?.id != R.id.weatherEldersFragment)
                navController.navigate(R.id.action_weatherFragment_to_weatherEldersFragment)
        }
    }

}