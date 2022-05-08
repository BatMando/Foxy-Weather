package com.mando.foxyweatherapp.utitlity.broadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mando.foxyweatherapp.utitlity.NetworkUtility


class NetworkChangeReceiver(context: Context?) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val status: Int = NetworkUtility.getConnectivityStatusString(context!!)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            isThereInternetConnection = status != NetworkUtility.NETWORK_STATUS_NOT_CONNECTED
        }
    }

    companion object {
        var isThereInternetConnection = false
    }
}