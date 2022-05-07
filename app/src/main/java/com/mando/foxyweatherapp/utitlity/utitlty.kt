package com.mando.foxyweatherapp.utitlity

import android.content.Context
import android.location.Geocoder
import com.mando.foxyweatherapp.R
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun getIcon(imageString: String): Int {
    val imageInInteger: Int
    when (imageString) {
        "01d" -> imageInInteger = R.drawable.icon_01d
        "01n" -> imageInInteger = R.drawable.icon_01n
        "02d" -> imageInInteger = R.drawable.icon_02d
        "02n" -> imageInInteger = R.drawable.icon_02n
        "03n" -> imageInInteger = R.drawable.icon_03n
        "03d" -> imageInInteger = R.drawable.icon_03d
        "04d" -> imageInInteger = R.drawable.icon_04d
        "04n" -> imageInInteger = R.drawable.icon_04n
        "09d" -> imageInInteger = R.drawable.icon_09d
        "09n" -> imageInInteger = R.drawable.icon_09n
        "10d" -> imageInInteger = R.drawable.icon_10d
        "10n" -> imageInInteger = R.drawable.icon_10n
        "11d" -> imageInInteger = R.drawable.icon_11d
        "11n" -> imageInInteger = R.drawable.icon_11n
        "13d" -> imageInInteger = R.drawable.icon_13d
        "13n" -> imageInInteger = R.drawable.icon_13n
        "50d" -> imageInInteger = R.drawable.icon_50d
        "50n" -> imageInInteger = R.drawable.icon_50n
        else -> imageInInteger = R.drawable.ic_weather_condition
    }
    return imageInInteger
}

fun getCityText(context: Context, lat: Double, lon: Double, language: String): String {
    var city = "Unknown!"
    val geocoder = Geocoder(context, Locale(language))
    try {
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            city = "${addresses[0].adminArea}, ${addresses[0].countryName}"
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return city
}

fun convertLongToTime(time: Long): String {
    val date = Date(TimeUnit.SECONDS.toMillis(time))
    val format = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return format.format(date)
}
fun getDayOfWeek(timestamp: Long): String {
    return SimpleDateFormat("EEEE", Locale.ENGLISH).format(timestamp * 1000)
}

fun longToDateAsString(dateInMillis: Long): String {
    val d = Date(dateInMillis * 1000)
    val dateFormat: DateFormat = SimpleDateFormat("d MMM, yyyy")
    return dateFormat.format(d)
}


