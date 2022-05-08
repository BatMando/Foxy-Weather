package com.mando.foxyweatherapp.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mando.foxyweatherapp.MainActivity
import com.mando.foxyweatherapp.R
import kotlinx.coroutines.*

class SplashScreen : AppCompatActivity() {
    private val parentJob = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
        coroutineScope.launch {
            delay(4000)
            startMainScreen()
        }
    }

    private fun startMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}