package com.mando.foxyweatherapp.model.responseModels

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("dt") var dt: Long,
    @SerializedName("temp") var temp: Double,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("uvi") var uvi: Double,
    @SerializedName("clouds") var clouds: Int,
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("wind_speed") var windSpeed: Double,
    @SerializedName("weather") var weather: List<Weather>
)