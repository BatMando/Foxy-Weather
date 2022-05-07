package com.mando.foxyweatherapp.homeScreen.viewModel

import androidx.lifecycle.*
import com.example.moviesappmvvm.model.RepositoryInterface

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val _myWeather: MutableLiveData<WeatherResponse> = MutableLiveData()
    val myMovies: LiveData<WeatherResponse> = _myWeather

    fun getWeatherFromNetwork(lat:Double , lon:Double, units:String , lang:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherResponse = repo.getWeatherFromNetwork(lat, lon, units, lang)
            //Log.e("AllMoviesViewModel", "getAllMovies: "+ (allMovies.body()?.size ?: 0))
            if (weatherResponse.isSuccessful){
                _myWeather.postValue(weatherResponse.body())
            }

        }
    }
}

class HomeFragmentViewModelFactory(val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java))
            return HomeFragmentViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}