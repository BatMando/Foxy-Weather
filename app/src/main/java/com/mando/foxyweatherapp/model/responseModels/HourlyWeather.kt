package com.mando.foxyweatherapp.model.responseModels

import com.google.gson.annotations.SerializedName

data class HourlyWeather(
    @SerializedName("dt") var dt: Long,
    @SerializedName("temp") var temp: Double,
    @SerializedName("weather") var weather: List<Weather>
)
