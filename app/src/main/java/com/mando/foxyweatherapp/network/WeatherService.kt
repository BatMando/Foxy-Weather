package com.mando.foxyweatherapp.network

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    //https://api.openweathermap.org/data/2.5/onecall?lat=30.044420&lon=31.235712&units=metric&lang=ar&appid=3d0b0de49fc523ca76e35ed208b38173
    @GET("onecall")
    suspend fun getWeather(@Query("lat")lat:Double , @Query("lon")lon:Double,@Query("units")units:String,@Query("lang")lang:String,@Query("appid")appid:String): Response<WeatherResponse>
}