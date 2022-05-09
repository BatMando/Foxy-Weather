package com.mando.foxyweatherapp.alertsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mando.foxyweatherapp.model.repo.RepositoryInterface

class AlertsFragmentViewModelFactory(val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertsFragmentViewModel::class.java))
            return AlertsFragmentViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}