package com.mando.foxyweatherapp.addalarmscreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mando.foxyweatherapp.model.repo.RepositoryInterface

class AddAlertFragmentViewModelFactory(val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAlertFragmentViewModel::class.java))
            return AddAlertFragmentViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}