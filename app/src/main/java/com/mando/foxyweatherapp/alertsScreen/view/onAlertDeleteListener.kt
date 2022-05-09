package com.mando.foxyweatherapp.alertsScreen.view

import com.mando.foxyweatherapp.model.alertsModel.Alerts

interface onAlertDeleteListener {
    fun deleteAlert(alerts: Alerts)
}