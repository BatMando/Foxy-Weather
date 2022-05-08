package com.mando.foxyweatherapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse

class ConcreteLocalSource (context: Context) : LocalSource {
    private val weatherDao :WeatherDao?
    private val alertsDao :AlertsDao?
    private val favouritesDao :FavouritesDao?



    init {
        val db: AppDataBase? = AppDataBase.getInstance(context)
        weatherDao = db?.weatherDao()
        alertsDao = db?.alertsDao()
        favouritesDao = db?.favouritesDao()



    }




    override suspend fun insertWeather(weather: WeatherResponse) {
        weatherDao?.insertWeather(weather)
    }

    override suspend fun allStoredWeather(): WeatherResponse {
        return weatherDao?.getAllWeather()!!
    }

    override fun insertFavourite(fav: FavouriteLocation) {
        favouritesDao?.insertFavourite(fav)
    }

    override fun deleteFavourite(fav: FavouriteLocation) {
        favouritesDao?.deleteFavourite(fav)
    }

    override suspend fun allStoredFavourites(): LiveData<List<FavouriteLocation>> {
        return favouritesDao?.getAllFavourites()!!
    }

    override fun insertAlert(alert: Alerts) {
        alertsDao?.insertAlert(alert)
    }

    override fun deleteAlert(alert: Alerts) {
        alertsDao?.deleteAlert(alert)
    }

    override suspend fun allStoredAlerts(): LiveData<List<Alerts>> {
        return alertsDao?.getAllAlerts()!!
    }


}