package com.mando.foxyweatherapp.model.alertsModel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class Alerts(var startDate:Long,
                  var endDate:Long,
                  var alertTime :Long,
                  var alertType :String,
                  var alertDays: List<String>,
                  var lat: Double,
                  var lon: Double,
                  @PrimaryKey(autoGenerate = true)
                  @NonNull
                  val id: Int? = null
)

