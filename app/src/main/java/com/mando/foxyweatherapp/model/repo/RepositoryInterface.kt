package com.mando.foxyweatherapp.model.repo

import androidx.lifecycle.LiveData
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import retrofit2.Response

interface RepositoryInterface {
    suspend fun getWeatherFromNetwork(lat:Double, lon:Double, units:String, lang:String, isFavourite: Boolean = false): Response<WeatherResponse>

    suspend fun insertWeather(weather: WeatherResponse)
    suspend fun allStoredWeather(): WeatherResponse

    fun insertFavourite(fav: FavouriteLocation)
    fun deleteFavourite(fav: FavouriteLocation)
    fun allStoredFavourites(): LiveData<List<FavouriteLocation>>

    fun insertAlert(alert: Alerts)
    fun deleteAlert(alert: Alerts)
    suspend fun allStoredAlerts(): LiveData<List<Alerts>>
}