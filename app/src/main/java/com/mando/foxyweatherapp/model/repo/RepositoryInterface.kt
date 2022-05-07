package com.example.moviesappmvvm.model

import androidx.lifecycle.LiveData
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import retrofit2.Response

interface RepositoryInterface {
    suspend fun getWeatherFromNetwork(lat:Double , lon:Double, units:String , lang:String): Response<WeatherResponse>

//    val storedMovies :LiveData<List<Movie>>
//    fun insertMovie(movie: Movie)
//    fun deleteMovie(movie: Movie)


}