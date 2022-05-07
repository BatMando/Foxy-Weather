package com.mando.foxyweatherapp.model.responseModels

import com.google.gson.annotations.SerializedName



data class WeatherResponse(
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
    @SerializedName("current") var current: CurrentWeather,
    @SerializedName("hourly") var hourly: List<HourlyWeather>,
    @SerializedName("daily") var daily: List<DailyWeather>,
)
