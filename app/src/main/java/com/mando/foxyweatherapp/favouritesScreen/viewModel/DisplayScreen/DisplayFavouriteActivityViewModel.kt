package com.mando.foxyweatherapp.favouritesScreen.viewModel.DisplayScreen

import android.util.Log
import androidx.lifecycle.*
import com.mando.foxyweatherapp.model.repo.RepositoryInterface

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import com.mando.foxyweatherapp.utitlity.MyLocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response

class DisplayFavouriteActivityViewModel(
    private val repo: RepositoryInterface
) : ViewModel() {

    private val _myWeather = MutableLiveData<WeatherResponse>()
    val myWeather :LiveData<WeatherResponse> = _myWeather

    fun getDataFromRemoteToLocal(lat: Double, long: Double, language: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val allMovies = repo.getWeatherFromNetwork(lat, long, units, language , true)
            _myWeather.postValue(allMovies.body())
        }
    }
}

