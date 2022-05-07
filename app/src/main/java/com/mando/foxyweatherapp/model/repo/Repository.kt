package com.example.moviesappmvvm.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse

import com.mando.foxyweatherapp.network.RemoteSource
import retrofit2.Response

class Repository private constructor(var remoteSource: RemoteSource, var context: Context):RepositoryInterface{

    companion object{
        private var instance:Repository?= null
        fun getInstance( remoteSource: RemoteSource,  context: Context):Repository{
            return instance?: Repository(remoteSource, context)
        }
    }
    override suspend fun getWeatherFromNetwork(lat:Double , lon:Double, units:String , lang:String): Response<WeatherResponse>{
        return remoteSource.getWeatherFromNetwork(lat,lon, units, lang)
    }

//    override val storedMovies: LiveData<List<Movie>>
//        get() = localSource.allStoredMovies
//
//    override fun insertMovie(movie: Movie) {
//        localSource.insertMovie(movie)
//    }
//
//    override fun deleteMovie(movie: Movie) {
//        localSource.deleteMovie(movie)
//    }

}