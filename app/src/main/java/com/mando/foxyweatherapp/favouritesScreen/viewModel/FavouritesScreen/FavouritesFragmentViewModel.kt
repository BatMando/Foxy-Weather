package com.mando.foxyweatherapp.favouritesScreen.viewModel.FavouritesScreen

import androidx.lifecycle.*
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.repo.RepositoryInterface

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import com.mando.foxyweatherapp.utitlity.MyLocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response

class FavouritesFragmentViewModel(
    private val repo: RepositoryInterface

) : ViewModel() {


//    private val _myFavouriteLocation = MutableLiveData<List<FavouriteLocation>>()
//    var myWeather :LiveData<List<FavouriteLocation>> = _myFavouriteLocation
//


    fun getFavouritesFromDatabase() :LiveData<List<FavouriteLocation>> {
//        viewModelScope.launch(Dispatchers.IO) {
//            _myFavouriteLocation.postValue(
//                repo.allStoredWeather()
//            )
//        }
        return repo.allStoredFavourites()
    }
    fun deleteFavourite(fav: FavouriteLocation){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavourite(fav)
        }
    }

}

