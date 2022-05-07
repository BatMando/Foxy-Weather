package com.mando.foxyweatherapp.network

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import retrofit2.Response


interface RemoteSourceInterface {
    suspend fun getWeatherFromNetwork(lat:Double , lon:Double, units:String , lang:String): Response<WeatherResponse>
}