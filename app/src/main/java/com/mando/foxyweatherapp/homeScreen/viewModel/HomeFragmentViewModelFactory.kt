package com.mando.foxyweatherapp.homeScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mando.foxyweatherapp.model.repo.RepositoryInterface
import com.mando.foxyweatherapp.utitlity.MyLocationProvider

class HomeFragmentViewModelFactory(val repo: RepositoryInterface, val myLocationProvider: MyLocationProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java))
            return HomeFragmentViewModel(repo,myLocationProvider) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}