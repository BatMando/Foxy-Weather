package com.mando.foxyweatherapp.model.alertsModel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class Alerts(var startDate:Long,
                  var endDate:Long,
                  @PrimaryKey(autoGenerate = true)
                  var id:Int
)
