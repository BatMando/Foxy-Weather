package com.mando.foxyweatherapp.model.responseModels

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "weather")
data class WeatherResponse(
    @PrimaryKey
    @NonNull
    var id: Int = 0 ,
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
    @SerializedName("current") var current: CurrentWeather,
    @SerializedName("hourly") var hourly: List<HourlyWeather>,
    @SerializedName("daily") var daily: List<DailyWeather>,
)
