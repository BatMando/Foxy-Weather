package com.mando.foxyweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {
    @Query("select * from alerts")
    fun getAllAlerts(): LiveData<List<Alerts>>

    @Insert(onConflict = REPLACE)
    fun insertAlert(alert: Alerts)

    @Delete
    fun deleteAlert(alert: Alerts)

}