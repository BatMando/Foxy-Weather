package com.mando.foxyweatherapp.favouritesScreen.view.DisplayScreen

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesappmvvm.model.Repository
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.database.ConcreteLocalSource
import com.mando.foxyweatherapp.favouritesScreen.viewModel.DisplayScreen.DisplayFavouriteActivityViewModel
import com.mando.foxyweatherapp.favouritesScreen.viewModel.DisplayScreen.DisplayFavouriteActivityViewModelFactory
import com.mando.foxyweatherapp.homeScreen.view.Past24HoursForecastRecyclerAdapter
import com.mando.foxyweatherapp.homeScreen.view.Past7DaysForecastRecyclerAdapter
import com.mando.foxyweatherapp.homeScreen.viewModel.HomeFragmentViewModelFactory
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.*

@Suppress("DEPRECATION")
class DisplayFavouriteActivity : AppCompatActivity() {
    private lateinit var daysForecastRecyclerAdapter: Past7DaysForecastRecyclerAdapter
    private lateinit var daysLayoutManger: RecyclerView.LayoutManager
    private lateinit var daysRecyclerView: RecyclerView
    private lateinit var hoursForecastRecyclerAdapter: Past24HoursForecastRecyclerAdapter
    private lateinit var hoursLayoutManger: RecyclerView.LayoutManager
    private lateinit var hoursRecyclerView: RecyclerView
    private lateinit var displayFavouriteActivityViewModel:DisplayFavouriteActivityViewModel
    private lateinit var displayFavouriteActivityViewModelFactory: DisplayFavouriteActivityViewModelFactory

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
    private lateinit var title :TextView
    private lateinit var backBtn : ImageView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_favourite)
        setValuesRequiredForApi()
        openLoadingDialog()
        initViewModel()
        initView()
        setListeners()
        getDataFromApi()
        observeWeather()

    }

    private fun getDataFromApi() {
        displayFavouriteActivityViewModel.getDataFromRemoteToLocal(language = language,lat = latitude,long = longitude,units = units)
    }

    private fun setValuesRequiredForApi() {
        latitude = intent.getDoubleExtra("lat",0.0)
        longitude = intent.getDoubleExtra("lon",0.0)
        getSharedPreferences(this).apply {
            language = getString(getString(R.string.languageSetting), "en") ?: "en"
            units = getString(getString(R.string.unitsSetting), "metric") ?: "metric"
        }
    }

    private fun openLoadingDialog(){
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(getString(R.string.loading))
        progressDialog.setMessage(getString(R.string.applicationLoading))
        progressDialog.show()
    }

    private fun initViewModel(){
        displayFavouriteActivityViewModelFactory = DisplayFavouriteActivityViewModelFactory(
            Repository.getInstance(
                RemoteSource.getInstance(), ConcreteLocalSource(this), this
            ))

        displayFavouriteActivityViewModel =
            ViewModelProvider(this, displayFavouriteActivityViewModelFactory)[DisplayFavouriteActivityViewModel::class.java]
    }

    private fun setListeners(){
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        tvLocation = findViewById(R.id.tv_location)
        tvCurrentTemp = findViewById(R.id.tv_current_temp)
        tvTempUnit = findViewById(R.id.tv_temp_unit)
        tvTempState = findViewById(R.id.tv_temp_state)
        tvDate = findViewById(R.id.tv_date)
        tvDayTemp = findViewById(R.id.tv_day_temp)
        pressureTv = findViewById(R.id.pressure_tv)
        humidityTv = findViewById(R.id.humidity_tv)
        windTv = findViewById(R.id.wind_tv)
        cloudsTv = findViewById(R.id.clouds_tv)
        uvTv = findViewById(R.id.uv_tv)
        visibilityTv = findViewById(R.id.visibility_tv)
        backBtn = findViewById(R.id.backBtn)
        title = findViewById(R.id.favouriteTitle)

        daysRecyclerView = findViewById(R.id.past_7days_recycler)
        hoursRecyclerView = findViewById(R.id.past_24hrs_recycler)

        daysForecastRecyclerAdapter = Past7DaysForecastRecyclerAdapter()
        hoursForecastRecyclerAdapter = Past24HoursForecastRecyclerAdapter()

        daysLayoutManger = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        hoursLayoutManger = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        daysRecyclerView.layoutManager = daysLayoutManger
        hoursRecyclerView.layoutManager = hoursLayoutManger

        daysRecyclerView.adapter = daysForecastRecyclerAdapter
        hoursRecyclerView.adapter = hoursForecastRecyclerAdapter
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

    private fun setDataToViews(weatherResponse: WeatherResponse, context: Context) {
        title.text = intent.getStringExtra("title")
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
        } else {
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
        daysForecastRecyclerAdapter.setValuesFromSharedPreferences(this)
        hoursForecastRecyclerAdapter.setValuesFromSharedPreferences(this)

        daysForecastRecyclerAdapter.notifyDataSetChanged()
        hoursForecastRecyclerAdapter.notifyDataSetChanged()
    }

    private fun observeWeather(){
        displayFavouriteActivityViewModel.myWeather.observe(this) { weather ->
            setDataToViews(weather, this)
            progressDialog.dismiss()
        }
    }
}