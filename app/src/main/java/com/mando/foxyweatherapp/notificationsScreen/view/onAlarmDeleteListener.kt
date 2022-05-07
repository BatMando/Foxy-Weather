package com.mando.foxyweatherapp.notificationsScreen.view

import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation

interface onAlarmDeleteListener {
    fun deleteAlarm(alerts: Alerts)
}