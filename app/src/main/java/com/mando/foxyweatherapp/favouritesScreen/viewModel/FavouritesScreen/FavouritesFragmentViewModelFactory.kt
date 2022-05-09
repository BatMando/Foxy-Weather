package com.mando.foxyweatherapp.favouritesScreen.viewModel.FavouritesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mando.foxyweatherapp.model.repo.RepositoryInterface

class FavouritesFragmentViewModelFactory(val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritesFragmentViewModel::class.java))
            return FavouritesFragmentViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}