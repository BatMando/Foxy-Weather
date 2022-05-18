package com.mando.foxyweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.responseModels.HourlyWeather
import com.mando.foxyweatherapp.utitlity.convertLongToTime
import com.mando.foxyweatherapp.utitlity.convertNumbersToArabic
import com.mando.foxyweatherapp.utitlity.getIcon
import com.mando.foxyweatherapp.utitlity.getSharedPreferences

class Past24HoursForecastRecyclerAdapter :
    RecyclerView.Adapter<Past24HoursForecastRecyclerAdapter.ViewHolder>() {

    private var language: String = "en"
    private var units: String = "metric"
    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String

    var hourlyWeather: List<HourlyWeather> = arrayListOf()

    inner class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val day: TextView = itemView.findViewById(R.id.time_tv)
        val temp: TextView = itemView.findViewById(R.id.temp_tv)
        val img: ImageView = itemView.findViewById(R.id.weather_condition_img)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item_hourly_forecast, parent, false)
        return ViewHolder(view)
    }

    fun setValuesFromSharedPreferences(context: Context) {
        getSharedPreferences(context).apply {
            language = getString(context.getString(R.string.languageSetting), "en") ?: "en"
            units = getString(context.getString(R.string.unitsSetting), "metric") ?: "metric"
        }
    }

    override fun getItemCount(): Int = hourlyWeather.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        if (language == "ar") {
            setArabicUnit(units)
            viewHolder.img.setImageResource(getIcon(hourlyWeather[position].weather[0].icon))
            viewHolder.day.text = convertLongToTime(hourlyWeather[position].dt, language)
            viewHolder.temp.text =
                "${convertNumbersToArabic(hourlyWeather[position].temp.toInt())}$temperatureUnit"
        } else {
            setEnglishUnits(units)
            viewHolder.img.setImageResource(getIcon(hourlyWeather[position].weather[0].icon))
            viewHolder.day.text = convertLongToTime(hourlyWeather[position].dt, language)
            viewHolder.temp.text = "${hourlyWeather[position].temp.toInt()}$temperatureUnit"
        }

    }

    private fun setArabicUnit(units: String) {
        when (units) {
            "metric" -> {
                temperatureUnit = " °م"
                windSpeedUnit = " م/ث"
            }
            "imperial" -> {
                temperatureUnit = " °ف"
                windSpeedUnit = " ميل/س"
            }
        }
    }

    private fun setEnglishUnits(units: String) {
        when (units) {
            "metric" -> {
                temperatureUnit = " °C"
                windSpeedUnit = " m/s"
            }
            "imperial" -> {
                temperatureUnit = " °F"
                windSpeedUnit = " miles/h"
            }
        }
    }
}