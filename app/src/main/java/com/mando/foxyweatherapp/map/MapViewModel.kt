package com.mando.foxyweatherapp.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesappmvvm.model.Repository
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MapViewModel(private val repository: Repository): ViewModel() {

    fun setFavorite(lat: Double, lon: Double, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var favouriteLocation = FavouriteLocation(lat,lon,name)
                repository.insertFavourite(favouriteLocation)
            } catch (e: Exception) {
                throw e
            }
            this.cancel()
        }
    }

}