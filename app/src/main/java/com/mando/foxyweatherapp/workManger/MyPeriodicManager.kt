package com.mando.foxyweatherapp.workManger

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.example.moviesappmvvm.model.Repository
import com.mando.foxyweatherapp.database.ConcreteLocalSource
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.getCurrentDay
import io.reactivex.Single
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

import java.util.*
import java.util.concurrent.TimeUnit

class MyPeriodicManager (private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    var list :List<Alerts> = listOf()
    var delay: Long = 0
    var timeNow: Long = 0
    val repository = Repository.getInstance(
        RemoteSource.getInstance(), ConcreteLocalSource(context), context
    )

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result = coroutineScope {
        setAlertsToList()
        getTimePeriod()

        val worker = this@MyPeriodicManager

        // test coroutines
        val d = async {
            delay(1000)
            getCurrentData()
        }
        val value = d.await()

        Result.success()
    }
    private fun setAlertsToList(){

        GlobalScope.launch(Dispatchers.IO) {
            repository.localSource.getAllAlertsFlow().subscribe { alerts->
                list =alerts
                Log.e("mando", "getAllAlertsFlow: ${list.size}" )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getCurrentData() {
        val currentWeather = repository.allStoredWeather()

        if (list != null) {
            Log.e("MyPeriodicManager","list not null")
            for (alert in list) {
                Log.e("MyPeriodicManager","list not null")

                if (alert.alertDays.stream()
                        .anyMatch { s -> s.contains(getCurrentDay().toString()) }) {
                    if (checkPeriod(alert.alertTime)) {
                        Log.e("MyPeriodicManager", "anyMatch")
                        if (currentWeather.alerts.isNullOrEmpty()) {
                            Log.e("MyPeriodicManager", "checkPeriod")
                                setOneTimeWorkManger(
                                    delay,
                                    alert.id,
                                    currentWeather.current.weather[0].description,
                                    currentWeather.current.weather[0].icon,
                                    alert.alertType
                                )
                            }
                            else {
                                setOneTimeWorkManger(
                                    delay,
                                    alert.id,
                                    currentWeather.alerts!![0].tags[0],
                                    currentWeather.current.weather[0].icon,
                                    alert.alertType
                                )
                            }
                        }
                }
            }
        }

    }

    private fun checkPeriod(medTime: Long): Boolean {
        delay = medTime - timeNow
        Log.e("mando", "delay: $delay , $delay" )

        return delay > 0
    }

    private fun getTimePeriod() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        timeNow = (hour * 60).toLong()
        timeNow = ((timeNow + minute) * 60 ) - 7200
        Log.e("mando", "getTimePeriod: time now $timeNow" )

    }

    private fun setOneTimeWorkManger(delay: Long, id: Int?, description: String, icon: String , alertType:String) {
        val data = Data.Builder()
        data.putString("description", description)
        data.putString("icon", icon)
        data.putString("alertType", alertType)
        Log.e("TAG", "setOneTimeWorkManger: $alertType ")
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyOneTimeWorkManger::class.java)
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance().enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )

    }




}