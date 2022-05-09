package com.example.moviesappmvvm.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.mando.foxyweatherapp.database.LocalSource
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.repo.RepositoryInterface
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse

import com.mando.foxyweatherapp.network.RemoteSource
import retrofit2.Response

class Repository private constructor(var remoteSource: RemoteSource,var localSource: LocalSource,  var context: Context):
    RepositoryInterface {

    companion object{
        private var instance:Repository?= null
        fun getInstance( remoteSource: RemoteSource, localSource: LocalSource, context: Context):Repository{
            return instance?: Repository(remoteSource, localSource ,context)
        }
    }
    override suspend fun getWeatherFromNetwork(lat:Double , lon:Double, units:String , lang:String ,isFavourite :Boolean ): Response<WeatherResponse>{
        val result =remoteSource.getWeatherFromNetwork(lat,lon, units, lang)
        if (result.isSuccessful && !isFavourite){
            insertWeather(result.body()!!)
        }

        return result
    }

    override suspend fun allStoredWeather(): WeatherResponse {
        return localSource.allStoredWeather()
    }

    override suspend fun insertWeather(weather: WeatherResponse) {
        localSource.insertWeather(weather)
    }


    override fun insertFavourite(fav: FavouriteLocation) {
        localSource.insertFavourite(fav)
    }

    override fun deleteFavourite(fav: FavouriteLocation) {
        localSource.deleteFavourite(fav)
    }

    override fun allStoredFavourites(): LiveData<List<FavouriteLocation>> {
        return localSource.allStoredFavourites()
    }

    override fun insertAlert(alert: Alerts) {
        localSource.insertAlert(alert)
    }

    override fun deleteAlert(alert: Alerts) {
        localSource.deleteAlert(alert)
    }

    override suspend fun allStoredAlerts(): LiveData<List<Alerts>> {
        return localSource.allStoredAlerts()
    }



}