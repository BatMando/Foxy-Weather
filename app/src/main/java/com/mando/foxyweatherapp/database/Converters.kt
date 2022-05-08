package com.mando.foxyweatherapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mando.foxyweatherapp.model.responseModels.CurrentWeather
import com.mando.foxyweatherapp.model.responseModels.DailyWeather
import com.mando.foxyweatherapp.model.responseModels.HourlyWeather
import com.mando.foxyweatherapp.model.responseModels.Weather

class Converters {

    @TypeConverter
    fun currentToJson(current: CurrentWeather?) = Gson().toJson(current)

    @TypeConverter
    fun jsonToCurrent(currentString: String) =
        Gson().fromJson(currentString, CurrentWeather::class.java)

    @TypeConverter
    fun hourlyListToJson(hourlyList: List<HourlyWeather>?) = Gson().toJson(hourlyList)

    @TypeConverter
    fun jsonToHourlyList(hourlyString: String) =
        Gson().fromJson(hourlyString, Array<HourlyWeather>::class.java)?.toList()

    @TypeConverter
    fun dailyListToJson(dailyList: List<DailyWeather>) = Gson().toJson(dailyList)

    @TypeConverter
    fun jsonToDailyList(dailyString: String) =
        Gson().fromJson(dailyString, Array<DailyWeather>::class.java).toList()

    @TypeConverter
    fun weatherListToJson(weatherList: List<Weather>) = Gson().toJson(weatherList)

    @TypeConverter
    fun jsonToWeatherList(weatherString: String) =
        Gson().fromJson(weatherString, Array<Weather>::class.java).toList()

}