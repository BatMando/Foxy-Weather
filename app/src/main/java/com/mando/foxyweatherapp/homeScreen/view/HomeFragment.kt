package com.mando.foxyweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesappmvvm.model.Repository
import com.google.android.material.snackbar.Snackbar
import com.mando.foxyweatherapp.MainActivity
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.database.ConcreteLocalSource
import com.mando.foxyweatherapp.database.LocalSource
import com.mando.foxyweatherapp.homeScreen.viewModel.HomeFragmentViewModel
import com.mando.foxyweatherapp.homeScreen.viewModel.HomeFragmentViewModelFactory
import com.mando.foxyweatherapp.map.view.MapActivity
import com.mando.foxyweatherapp.model.responseModels.*
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.*
import com.mando.foxyweatherapp.utitlity.broadCast.NetworkChangeReceiver
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class HomeFragment : Fragment(),NetworkChangeReceiver.ConnectivityReceiverListener {

    private lateinit var daysForecastRecyclerAdapter: Past7DaysForecastRecyclerAdapter
    private lateinit var daysLayoutManger: RecyclerView.LayoutManager
    private lateinit var daysRecyclerView: RecyclerView
    private lateinit var hoursForecastRecyclerAdapter: Past24HoursForecastRecyclerAdapter
    private lateinit var hoursLayoutManger: RecyclerView.LayoutManager
    private lateinit var hoursRecyclerView: RecyclerView
    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel


    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var language: String = "en"
    private var units: String = "metric"
    private var flagNoConnection: Boolean = false


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
        setValuesFromSharedPreferences()

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

        NetworkChangeReceiver.connectivityReceiverListener = this
        activity?.registerReceiver(NetworkChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle(getString(R.string.loading))
        progressDialog.setMessage(getString(R.string.applicationLoading))
        progressDialog.show()

        homeFragmentViewModelFactory = HomeFragmentViewModelFactory(
            Repository.getInstance(
                RemoteSource.getInstance(), ConcreteLocalSource(requireContext()), requireContext()
            ), MyLocationProvider(this)
        )
        homeFragmentViewModel =
            ViewModelProvider(this, homeFragmentViewModelFactory)[HomeFragmentViewModel::class.java]

        if (!flagNoConnection) {
            if (getIsMap()) {
                if (isSharedPreferencesLatAndLongNull(requireContext())){
                    val intent = Intent(requireContext(), MapActivity::class.java)
                    intent.putExtra("isFavourite", false)
                    startActivity(intent)
                }
                try {
                    homeFragmentViewModel.getDataFromRemoteToLocal(
                        latitude,
                        longitude,
                        language,
                        units
                    )
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("mando","getIsMap:lat $latitude, lon $longitude")
            } else {
                Log.e("mando","get location :lat $latitude, lon $longitude")
                val location = MyLocationProvider(this)
                if (location.checkPermission() && location.isLocationEnabled()) {
                    homeFragmentViewModel.getFreshLocation()
                }else {
                    homeFragmentViewModel.getFreshLocation()
                }
                homeFragmentViewModel.observeLocation().observe(this) {
                    if (it[0] != 0.0 && it[1] != 0.0) {
                        latitude = it[0]
                        longitude = it[1]
                        val local = getCurrentLocale(requireContext())
                        language = getSharedPreferences(requireContext()).getString(
                            getString(R.string.languageSetting),
                            local?.language
                        )!!
                        units = getSharedPreferences(requireContext()).getString(
                            getString(R.string.unitsSetting),
                            "metric"
                        )!!

                        val editor = getSharedPreferences(requireContext()).edit()
                        editor.putFloat(getString(R.string.lat), latitude.toFloat())
                        editor.putFloat(getString(R.string.lon), longitude.toFloat())
                        editor.putBoolean(getString(R.string.firstTime), false)
                        editor.putBoolean(getString(R.string.isMap), false)
                        editor.apply()
                        try {
                            homeFragmentViewModel.getDataFromRemoteToLocal(
                                latitude,
                                longitude,
                                language,
                                units
                            )
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        } else{
            Log.e("mando","no network:lat $latitude, lon $longitude")
            homeFragmentViewModel.getDataFromDatabase()
        }

        homeFragmentViewModel.myWeather.observe(this.viewLifecycleOwner) { weather ->
            setDataToViews(weather, requireContext())

            Log.e("mando", "onViewCreated: +${weather.current.dt}" )
            if (checkIfNight(weather.current.dt)){
                (requireActivity() as MainActivity).constraintLayout.background = resources.getDrawable(R.drawable.night)
            }else {
                (requireActivity() as MainActivity).constraintLayout.background = resources.getDrawable(R.drawable.day)
            }
            progressDialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToViews(weatherResponse: WeatherResponse, context: Context) {

        if (language == "ar") {
            setArabicUnit(units)
            tvCurrentTemp.text = convertNumbersToArabic(weatherResponse.current.temp.toInt())
            tvDayTemp.text =
                "${convertNumbersToArabic(weatherResponse.daily[0].temp.max.toInt())}$temperatureUnit/${
                    convertNumbersToArabic(weatherResponse.daily[0].temp.min.toInt())
                }$temperatureUnit"
            pressureTv.text = "${convertNumbersToArabic(weatherResponse.current.pressure)}  هب"
            humidityTv.text = "${convertNumbersToArabic(weatherResponse.current.humidity)}%"
            windTv.text =
                "${convertNumbersToArabic(weatherResponse.current.windSpeed)}$windSpeedUnit"
            cloudsTv.text = "${convertNumbersToArabic(weatherResponse.current.clouds)}  هب"
            uvTv.text = "${convertNumbersToArabic(weatherResponse.current.uvi.toInt())}%"
            visibilityTv.text =
                "${convertNumbersToArabic(weatherResponse.current.visibility)}$windSpeedUnit"
        }
        else {
            setEnglishUnits(units)
            tvCurrentTemp.text = "${weatherResponse.current.temp.toInt()}"
            tvDayTemp.text =
                "${weatherResponse.daily[0].temp.max.toInt()}$temperatureUnit/${weatherResponse.daily[0].temp.min.toInt()}$temperatureUnit"
            pressureTv.text = "${weatherResponse.current.pressure} hps"
            humidityTv.text = "${weatherResponse.current.humidity}%"
            windTv.text = "${weatherResponse.current.windSpeed}$windSpeedUnit"
            cloudsTv.text = "${weatherResponse.current.clouds} hps"
            uvTv.text = "${weatherResponse.current.uvi.toInt()}%"
            visibilityTv.text = "${weatherResponse.current.visibility}$windSpeedUnit"
        }
        tvLocation.text = getCityText(context, weatherResponse.lat, weatherResponse.lon, language)
        tvDate.text = longToDateAsString(weatherResponse.current.dt,language)
        tvTempState.text = weatherResponse.current.weather[0].description
        tvTempUnit.text = temperatureUnit

        daysForecastRecyclerAdapter.dailyWeather = weatherResponse.daily.drop(1)
        hoursForecastRecyclerAdapter.hourlyWeather = weatherResponse.hourly
        daysForecastRecyclerAdapter.setValuesFromSharedPreferences(requireContext())
        hoursForecastRecyclerAdapter.setValuesFromSharedPreferences(requireContext())

        daysForecastRecyclerAdapter.notifyDataSetChanged()
        hoursForecastRecyclerAdapter.notifyDataSetChanged()
    }

    private fun initView(view: View) {
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

        daysLayoutManger = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        hoursLayoutManger = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        daysRecyclerView.layoutManager = daysLayoutManger
        hoursRecyclerView.layoutManager = hoursLayoutManger

        daysRecyclerView.adapter = daysForecastRecyclerAdapter
        hoursRecyclerView.adapter = hoursForecastRecyclerAdapter
    }

    private fun setValuesFromSharedPreferences() {
        getSharedPreferences(requireContext()).apply {
            latitude = getFloat(getString(R.string.lat), 0.0f).toDouble()
            longitude = getFloat(getString(R.string.lon), 0.0f).toDouble()
            language = getString(getString(R.string.languageSetting), "en") ?: "en"
            units = getString(getString(R.string.unitsSetting), "metric") ?: "metric"
        }
    }

    private fun getIsMap(): Boolean {
        return getSharedPreferences(requireContext()).getBoolean(getString(R.string.isMap), false)
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
            "standard" -> {
                temperatureUnit = " °ك"
                windSpeedUnit = " م/ث"
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
            "standard" -> {
                temperatureUnit = " °K"
                windSpeedUnit = " m/s"
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            if (flagNoConnection) {

                flagNoConnection = false
            }
        } else {
            Toast.makeText(requireContext(),getString(R.string.noInternetConnection),Toast.LENGTH_SHORT).show()
            flagNoConnection = true
        }
    }
}