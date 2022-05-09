package com.mando.foxyweatherapp.database

import androidx.lifecycle.LiveData
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse

interface LocalSource {
    suspend fun insertWeather(weather: WeatherResponse)
    suspend fun allStoredWeather(): WeatherResponse

    fun insertFavourite(fav: FavouriteLocation)
    fun deleteFavourite(fav: FavouriteLocation)
    fun allStoredFavourites(): LiveData<List<FavouriteLocation>>

    fun insertAlert(alert: Alerts)
    fun deleteAlert(alert: Alerts)
    suspend fun allStoredAlerts(): LiveData<List<Alerts>>

}