package com.mando.foxyweatherapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mando.foxyweatherapp.model.responseModels.*

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

    @TypeConverter
    fun alertListToJson(alertList: List<AlertsResponse>?) = Gson().toJson(alertList)

    @TypeConverter
    fun jsonToAlertList(alertString: String?): List<AlertsResponse>? {
        alertString?.let {
            return Gson().fromJson(alertString, Array<AlertsResponse>::class.java)?.toList()
        }
        return emptyList()
    }

    @TypeConverter
    fun alertDaysListToJson(alertDays: List<String>) = Gson().toJson(alertDays)

    @TypeConverter
    fun jsonToAlertDaysList(alertDays: String) =
        Gson().fromJson(alertDays, Array<String>::class.java).toList()


}