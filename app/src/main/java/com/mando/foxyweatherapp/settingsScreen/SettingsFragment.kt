package com.mando.foxyweatherapp.settingsScreen

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mando.foxyweatherapp.MainActivity
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.map.view.MapActivity
import com.mando.foxyweatherapp.utitlity.getSharedPreferences


class SettingsFragment : Fragment() {

    private lateinit var newUnitSetting: String
    private lateinit var newLanguageSetting: String
    private var newLocationSetting: Boolean = false

    private lateinit var oldUnitSetting: String
    private lateinit var oldLanguageSetting: String
    private var oldLocationSetting: Boolean = false

    private lateinit var settingsEnglish: RadioButton
    private lateinit var settingsArabic: RadioButton
    private lateinit var radioGroupLanguage: RadioGroup
    private lateinit var settingsLocation: RadioButton
    private lateinit var settingsMap: RadioButton
    private lateinit var radioGroupLocation: RadioGroup
    private lateinit var unitCelsius: RadioButton
    private lateinit var unitFahrenheit: RadioButton
    private lateinit var radioGroupUnits: RadioGroup
    private lateinit var btnSave: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        initView(view)
        setSettings()
        btnSave.setOnClickListener {
            getLocationSettings()
            getLanguagesSettings()
            getUnitSettings()
            if (newLocationSetting && oldLocationSetting) {
                changeMapLocationDialog()
            } else {
                setSettingsToSharedPreferences()
                backToHomeScreen()
            }
        }
    }

    fun initView (view: View){
        settingsEnglish = view.findViewById<RadioButton>(R.id.settings_english)
        settingsArabic = view.findViewById<RadioButton>(R.id.settings_arabic)
        radioGroupLanguage = view.findViewById<RadioGroup>(R.id.radioGroupLanguage)
        settingsLocation = view.findViewById<RadioButton>(R.id.settings_location)
        settingsMap = view.findViewById<RadioButton>(R.id.settings_map)
        radioGroupLocation = view.findViewById<RadioGroup>(R.id.radioGroupLocation)
        unitCelsius = view.findViewById<RadioButton>(R.id.unit_celsius)
        unitFahrenheit = view.findViewById<RadioButton>(R.id.unit_fahrenheit)
        radioGroupUnits = view.findViewById<RadioGroup>(R.id.radioGroupUnits)
        btnSave = view.findViewById<Button>(R.id.btn_save)
    }

    private fun getUnitSettings() {
        when (radioGroupUnits.checkedRadioButtonId) {
            R.id.unit_celsius -> newUnitSetting = "metric"
            R.id.unit_fahrenheit -> newUnitSetting = "imperial"
        }
    }

    private fun getLanguagesSettings() {
        when (radioGroupLanguage.checkedRadioButtonId) {
            R.id.settings_arabic -> newLanguageSetting = "ar"
            R.id.settings_english -> newLanguageSetting = "en"
        }
    }

    private fun getLocationSettings() {
        when (radioGroupLocation.checkedRadioButtonId) {
            R.id.settings_map -> newLocationSetting = true
            R.id.settings_location -> newLocationSetting = false
        }
    }

    private fun setSettings() {
        getSettingsFromSharedPreferences()

        when (oldUnitSetting) {
            "metric" -> unitCelsius.isChecked = true
            "imperial" -> unitFahrenheit.isChecked = true
        }

        when (oldLanguageSetting) {
            "ar" -> settingsArabic.isChecked = true
            "en" -> settingsEnglish.isChecked = true
        }

        when (oldLocationSetting) {
            true -> settingsMap.isChecked = true
            false -> settingsLocation.isChecked = true
        }

    }

    private fun getSettingsFromSharedPreferences() {
        getSharedPreferences(requireContext()).apply {
            oldUnitSetting = getString(getString(R.string.unitsSetting), "metric")!!
            oldLanguageSetting = getString(getString(R.string.languageSetting), "en")!!
            oldLocationSetting = getBoolean(getString(R.string.isMap), false)
        }
    }

    private fun setSettingsToSharedPreferences() {
        getSharedPreferences(requireContext()).edit().apply {
            putString(getString(R.string.unitsSetting), newUnitSetting)
            putString(getString(R.string.languageSetting), newLanguageSetting)
            if (newLocationSetting && !oldLocationSetting) {
                resetLocationData()
            } else if (oldLocationSetting && !newLocationSetting) {
                resetLocationData()
            }
            putBoolean(getString(R.string.isMap), newLocationSetting)
            apply()
        }
    }

    private fun resetLocationData() {
        getSharedPreferences(requireContext()).edit().apply {
            remove(getString(R.string.lat))
            remove(getString(R.string.lon))
            apply()
        }
    }


    private fun backToHomeScreen() {
        val refresh = Intent(requireContext(), MainActivity::class.java)
        startActivity(refresh)
        activity?.finish()
    }

    private fun goToMapScreen() {
        val intent = Intent(requireContext(), MapActivity::class.java)
        intent.putExtra("isFavorite",false)
        startActivity(intent)
        activity?.finish()
    }

    private fun changeMapLocationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.map_dialog_title))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                setSettingsToSharedPreferences()
                backToHomeScreen()
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                resetLocationData()
                setSettingsToSharedPreferences()
                goToMapScreen()
                dialog.dismiss()
            }
            .show()
    }

}