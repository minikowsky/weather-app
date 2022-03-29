package com.example.weatherapp

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main)
        else*/
            setContentView(R.layout.activity_main)
    }


}