package com.mando.foxyweatherapp.addalarmscreen.viewModel

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

class AddAlertFragmentViewModel(
    private val repo: RepositoryInterface

) : ViewModel() {


    fun insertAlert( startDate:Long,
                     endDate:Long,
                     alertTime :Long,
                     alertType :String,
                     alertDays: List<String>,
                     lat: Double,
                     lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var alert = Alerts(startDate,endDate,alertTime,alertType,alertDays,lat,lon,null)
                repo.insertAlert(alert)
            } catch (e: Exception) {
                throw e
            }
            this.cancel()
        }
    }


}

