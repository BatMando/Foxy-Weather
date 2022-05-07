package com.mando.foxyweatherapp.network

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import retrofit2.Response


private const val appid="3d0b0de49fc523ca76e35ed208b38173"

class RemoteSource private constructor() : RemoteSourceInterface {


    companion object{
        private var instance: RemoteSource? = null
        fun getInstance(): RemoteSource {
            return instance ?: RemoteSource()
        }
    }
    override suspend fun getWeatherFromNetwork(lat:Double , lon:Double, units:String , lang:String): Response<WeatherResponse>{
       return RetrofitHelper.getInstance().create(WeatherService::class.java).getWeather(lat,lon,units,lang, appid)
    }

}