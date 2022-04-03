package com.example.weatherapp.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.tools.Unit

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get user preferences to get unit
        val sharedPref = (activity as MainActivity).applicationContext.getSharedPreferences("UserPreferences",MODE_PRIVATE)

        //setting current unit
        when(sharedPref.getString("Unit", Unit.METRIC)){
            Unit.METRIC -> view.findViewById<RadioButton>(R.id.units_radio_metric).isChecked = true
            Unit.IMPERIAL -> view.findViewById<RadioButton>(R.id.units_radio_imperial).isChecked = true
            Unit.STANDARD -> view.findViewById<RadioButton>(R.id.units_radio_standard).isChecked = true
        }
        //setting current application mode
        when(sharedPref.getBoolean("ElderMode", false)){
            false -> view.findViewById<Switch>(R.id.switch_elders).isChecked = false
            true -> view.findViewById<Switch>(R.id.switch_elders).isChecked = true
        }

        view.findViewById<RadioGroup>(R.id.units_radio_group)
            .setOnCheckedChangeListener{_, checkedId ->
                //when radiobutton is clicked(changed), update user preferences
            when(checkedId) {
                R.id.units_radio_metric ->
                    with (sharedPref.edit()) {
                        putString("Unit", Unit.METRIC)
                        apply()
                    }
                R.id.units_radio_imperial ->
                    with (sharedPref.edit()) {
                        putString("Unit", Unit.IMPERIAL)
                        apply()
                    }
                R.id.units_radio_standard ->
                    with (sharedPref.edit()) {
                        putString("Unit", Unit.STANDARD)
                        apply()
                    }
            }
        }

        view.findViewById<Switch>(R.id.switch_elders)
            .setOnCheckedChangeListener { _, isChecked ->
                //when application mode switch changes, update user preferences
                if(isChecked) {
                    with (sharedPref.edit()) {
                        putBoolean("ElderMode", true)
                        apply()
                    }
                } else {
                    with (sharedPref.edit()) {
                        putBoolean("ElderMode", false)
                        apply()
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_action_weather) {
            val sharedPref = (activity as MainActivity).applicationContext.getSharedPreferences("UserPreferences",MODE_PRIVATE)

            if(sharedPref.getBoolean("ElderMode",false)) {
                view?.findNavController()?.navigate(R.id.action_settingsFragment_to_weatherEldersFragment)
            } else {
                view?.findNavController()?.navigate(R.id.action_settingsFragment_to_weatherFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}