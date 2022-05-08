package com.mando.foxyweatherapp.homeScreen.view

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesappmvvm.model.Repository
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.homeScreen.viewModel.HomeFragmentViewModel
import com.mando.foxyweatherapp.homeScreen.viewModel.HomeFragmentViewModelFactory
import com.mando.foxyweatherapp.model.responseModels.*
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.getCityText
import com.mando.foxyweatherapp.utitlity.longToDateAsString
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private lateinit var daysForecastRecyclerAdapter: Past7DaysForecastRecyclerAdapter
    private lateinit var daysLayoutManger : RecyclerView.LayoutManager
    private lateinit var daysRecyclerView: RecyclerView
    private lateinit var hoursForecastRecyclerAdapter: Past24HoursForecastRecyclerAdapter
    private lateinit var hoursLayoutManger : RecyclerView.LayoutManager
    private lateinit var hoursRecyclerView: RecyclerView
    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel


    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var language: String = "en"
    private var units: String = "metric"

    private lateinit var tvLocation: TextView
    private lateinit var tvCurrentTemp: TextView
    private lateinit var tvTempUnit: TextView
    private lateinit var tvTempState: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDayTemp: TextView
    private lateinit var pressureTv: TextView
    private lateinit var humidityTv: TextView
    private lateinit var windTv: TextView
    private lateinit var cloudsTv: TextView
    private lateinit var uvTv: TextView
    private lateinit var visibilityTv: TextView
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Kotlin Progress Bar")
        progressDialog.setMessage("Application is loading, please wait")
        progressDialog.show()

        homeFragmentViewModelFactory = HomeFragmentViewModelFactory(Repository.getInstance(
            RemoteSource.getInstance(), requireContext()
        ))

        setEnglishUnits("metric")
        homeFragmentViewModel = ViewModelProvider(this,homeFragmentViewModelFactory).get(HomeFragmentViewModel::class.java)
        homeFragmentViewModel.getWeatherFromNetwork(29.9619891,30.9406877,"metric","en")
        //getAddress(29.9619891,30.9406877)
        homeFragmentViewModel.myMovies.observe(this.viewLifecycleOwner){ weather ->
          //  Log.e("mando", "onViewCreated: ${weather.current.temp}" )

            setDataToViews(weather,requireContext())
            progressDialog.hide()
        }
    }

    private fun setDataToViews(weatherResponse: WeatherResponse,context: Context){
        tvLocation.text = getCityText(context,weatherResponse.lat,weatherResponse.lon,"en")
        tvCurrentTemp.text = "${weatherResponse.current.temp.toInt()}"
        tvDate.text = longToDateAsString(weatherResponse.current.dt)
        tvTempState.text = weatherResponse.current.weather[0].description

        tvTempUnit.text = temperatureUnit
        tvDayTemp.text = "${weatherResponse.daily[0].temp.max.toInt()}$temperatureUnit/${weatherResponse.daily[0].temp.min.toInt()}$temperatureUnit"
        pressureTv.text = "${weatherResponse.current.pressure} hps"
        humidityTv.text = "${weatherResponse.current.humidity}%"
        windTv.text = "${weatherResponse.current.windSpeed}$windSpeedUnit"
        cloudsTv.text = "${weatherResponse.current.clouds} hps"
        uvTv.text = "${weatherResponse.current.uvi.toInt()}%"
        visibilityTv.text = "${weatherResponse.current.visibility}$windSpeedUnit"

        daysForecastRecyclerAdapter.dailyWeather = weatherResponse.daily
        hoursForecastRecyclerAdapter.hourlyWeather = weatherResponse.hourly
        daysForecastRecyclerAdapter.notifyDataSetChanged()
        hoursForecastRecyclerAdapter.notifyDataSetChanged()
    }

    private fun initView(view: View){
        tvLocation = view.findViewById(R.id.tv_location)
        tvCurrentTemp = view.findViewById(R.id.tv_current_temp)
        tvTempUnit = view.findViewById(R.id.tv_temp_unit)
        tvTempState = view.findViewById(R.id.tv_temp_state)
        tvDate = view.findViewById(R.id.tv_date)
        tvDayTemp = view.findViewById(R.id.tv_day_temp)
        pressureTv = view.findViewById(R.id.pressure_tv)
        humidityTv = view.findViewById(R.id.humidity_tv)
        windTv = view.findViewById(R.id.wind_tv)
        cloudsTv = view.findViewById(R.id.clouds_tv)
        uvTv = view.findViewById(R.id.uv_tv)
        visibilityTv = view.findViewById(R.id.visibility_tv)

        daysRecyclerView = view.findViewById(R.id.past_7days_recycler)
        hoursRecyclerView = view.findViewById(R.id.past_24hrs_recycler)

        daysForecastRecyclerAdapter = Past7DaysForecastRecyclerAdapter()
        hoursForecastRecyclerAdapter = Past24HoursForecastRecyclerAdapter()

        daysLayoutManger = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        hoursLayoutManger = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)

        daysRecyclerView.layoutManager = daysLayoutManger
        hoursRecyclerView.layoutManager = hoursLayoutManger

        daysRecyclerView.adapter = daysForecastRecyclerAdapter
        hoursRecyclerView.adapter = hoursForecastRecyclerAdapter
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
            "standard" -> {
                temperatureUnit = " °K"
                windSpeedUnit = " m/s"
            }
        }
    }
}