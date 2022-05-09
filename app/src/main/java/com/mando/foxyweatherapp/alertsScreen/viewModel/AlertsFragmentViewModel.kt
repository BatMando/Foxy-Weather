package com.mando.foxyweatherapp.alertsScreen.viewModel

import androidx.lifecycle.*
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.repo.RepositoryInterface

import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import com.mando.foxyweatherapp.utitlity.MyLocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response

class AlertsFragmentViewModel(
    private val repo: RepositoryInterface

) : ViewModel() {


    fun getAlertsFromDatabase() :LiveData<List<Alerts>> {
        return repo.allStoredAlerts()
    }

    fun deleteAlert(alert: Alerts){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlert(alert)
        }
    }

}

