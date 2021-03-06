package com.mando.foxyweatherapp.splashScreen

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.mando.foxyweatherapp.MainActivity
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.map.view.MapActivity
import com.mando.foxyweatherapp.utitlity.MyLocationProvider
import com.mando.foxyweatherapp.utitlity.broadCast.NetworkChangeReceiver
import com.mando.foxyweatherapp.utitlity.getCurrentLocale
import com.mando.foxyweatherapp.utitlity.getSharedPreferences

import kotlinx.coroutines.*
import java.util.*

class SplashScreen : AppCompatActivity() ,NetworkChangeReceiver.ConnectivityReceiverListener{
    private val parentJob = Job()
    lateinit var firstInitDialog: Dialog
    private var flagNoConnection: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        initConnectionListener()
        firstInitDialog = Dialog(this)
        val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
        coroutineScope.launch {
            delay(5000)
            if (!isFirstTime()){
                startMainScreen()
            }
            else{
                openFirstInitDialog()
            }
        }
    }

    private fun openFirstInitDialog() {
        firstInitDialog.setContentView(R.layout.first_init_dialog)
        firstInitDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var settingsLocation: RadioButton = firstInitDialog.findViewById<RadioButton>(R.id.settings_location)
        var settingsMap: RadioButton = firstInitDialog.findViewById<RadioButton>(R.id.settings_map)
        var radioGroupLocation: RadioGroup = firstInitDialog.findViewById<RadioGroup>(R.id.radioGroupLocation)
        val saveBtn: Button = firstInitDialog.findViewById<Button>(R.id.saveBtn)

        saveBtn.setOnClickListener {

            if (settingsLocation.isChecked || settingsMap.isChecked){
                when (radioGroupLocation.checkedRadioButtonId) {
                    R.id.settings_map -> {
                        startMapScreen()
                    }
                    R.id.settings_location -> {
                        startMainScreen()
                    }
                }
                firstInitDialog.dismiss()
            }
            else
            {
                Toast.makeText(this, getString(R.string.plsCheckMethod), Toast.LENGTH_SHORT).show()
            }

        }
        firstInitDialog?.show()
    }

    private fun startMainScreen() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
    }
    private fun startMapScreen(){
        getSharedPreferences(this).edit().apply {
            putBoolean(getString(R.string.isMap), true)
            apply()
        }
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun isFirstTime(): Boolean {
        return getSharedPreferences(this).getBoolean("firstTime", true)
    }

    private fun initConnectionListener() {

        NetworkChangeReceiver.connectivityReceiverListener = this
        this.registerReceiver(NetworkChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {

            if (isConnected) {
                if (flagNoConnection) {
                    flagNoConnection = false
                }
            } else {
                flagNoConnection = true
            }
}

}