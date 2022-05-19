package com.mando.foxyweatherapp.utitlity

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Build
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
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
    var city = "Unknown Location"
    val geocoder = Geocoder(context, Locale(language))
    try {
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            //Log.e("mando", "getCityText: ${addresses[0].featureName} , ${addresses[0].subAdminArea}  " )
            city = "${addresses[0].subAdminArea}, ${addresses[0].adminArea}, ${addresses[0].countryName}"
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return city
}

fun dateToLong(date: String?): Long {
    val f = SimpleDateFormat("dd-MM-yyyy")
    var milliseconds: Long = 0
    try {
        val d = f.parse(date)
        milliseconds = d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return milliseconds/1000
}

fun timeToSeconds(hour: Int, min: Int): Long {
    return (((hour * 60 + min) * 60) - 7200 ).toLong()
}

fun getCurrentDay(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    val date = Date()
    return dateFormat.format(date)
}

fun convertLongToTime(time: Long, language: String): String {
    val date = Date(TimeUnit.SECONDS.toMillis(time))
    val format = SimpleDateFormat("h:mm a",  Locale(language))
    return format.format(date)
}
fun getDayOfWeek(timestamp: Long, language: String): String {
    return SimpleDateFormat("EEEE",  Locale(language)).format(timestamp * 1000)
}

fun longToDateAsString(dateInMillis: Long, language: String): String {
    val d = Date(dateInMillis * 1000)
    val dateFormat: DateFormat = SimpleDateFormat("d MMM, yyyy",  Locale(language))
    return dateFormat.format(d)
}

fun checkIfNight(time:Long): Boolean {
    val date = Date(TimeUnit.SECONDS.toMillis(time))
    val format = SimpleDateFormat("kk", Locale.ENGLISH)
    if (format.format(date).toInt()>=18 || format.format(date).toInt()<4)
        return true
    return (false)
}

fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        context.getString(R.string.shared_pref),
        Context.MODE_PRIVATE
    )
}

fun isSharedPreferencesLatAndLongNull(context: Context): Boolean {
    val myPref = getSharedPreferences(context)
    val lat = myPref.getFloat(context.getString(R.string.lat), 0.0f)
    val long = myPref.getFloat(context.getString(R.string.lon), 0.0f)
    return lat == 0.0f && long == 0.0f
}


fun getCurrentLocale(context: Context): Locale? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0]
    } else {
        context.resources.configuration.locale
    }
}

fun convertNumbersToArabic(value: Double): String {
    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
}

fun convertNumbersToArabic(value: Int): String {
    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
}


