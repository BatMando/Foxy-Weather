package com.mando.foxyweatherapp.homeScreen.viewModel

import androidx.lifecycle.*
import com.mando.foxyweatherapp.model.repo.RepositoryInterface

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import com.mando.foxyweatherapp.utitlity.MyLocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeFragmentViewModel(
    private val repo: RepositoryInterface,
    private val myLocationProvider: MyLocationProvider
) : ViewModel() {


    private val _myWeather = MutableLiveData<WeatherResponse>()
    var myWeather :LiveData<WeatherResponse> = _myWeather


    fun getDataFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            _myWeather.postValue(
                repo.allStoredWeather()
            )
        }
    }

    fun getDataFromRemoteToLocal(lat: Double, long: Double, language: String, units: String) {

        var result : Response<WeatherResponse>? = null
        viewModelScope.launch(Dispatchers.Main) {
            val job =
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        result =
                            repo.getWeatherFromNetwork(lat, long, units, language)
                    } catch (e: Exception) {
                        result?.let { getDataFromDatabase() }
                    }
                }
            job.join()
            result?.let { getDataFromDatabase() }
            this.cancel()
        }
    }

    fun getFreshLocation() {
        myLocationProvider.getFreshLocation()
    }

    fun observeLocation(): LiveData<ArrayList<Double>> {
        return myLocationProvider.locationList
    }

}

