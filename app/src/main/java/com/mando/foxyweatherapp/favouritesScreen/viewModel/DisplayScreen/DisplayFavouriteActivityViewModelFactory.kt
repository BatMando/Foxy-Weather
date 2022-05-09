package com.mando.foxyweatherapp.favouritesScreen.viewModel.DisplayScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mando.foxyweatherapp.model.repo.RepositoryInterface
import com.mando.foxyweatherapp.utitlity.MyLocationProvider

class DisplayFavouriteActivityViewModelFactory(val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayFavouriteActivityViewModel::class.java))
            return DisplayFavouriteActivityViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}