package com.mando.foxyweatherapp.model.responseModels

import com.google.gson.annotations.SerializedName

data class DailyWeather(
    @SerializedName("dt") var dt: Long,
    @SerializedName("temp") var temp: Temp,
    @SerializedName("weather") var weather: List<Weather>
)
