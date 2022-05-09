package com.mando.foxyweatherapp.addalarmscreen.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.moviesappmvvm.model.Repository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.addalarmscreen.viewModel.AddAlertFragmentViewModel
import com.mando.foxyweatherapp.addalarmscreen.viewModel.AddAlertFragmentViewModelFactory
import com.mando.foxyweatherapp.utitlity.getSharedPreferences
import com.mando.foxyweatherapp.database.ConcreteLocalSource
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.convertLongToTime
import com.mando.foxyweatherapp.utitlity.dateToLong
import com.mando.foxyweatherapp.utitlity.timeToSeconds
import com.mando.foxyweatherapp.workManger.MyPeriodicManager
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AddAlarmActivity : AppCompatActivity() {

    private lateinit var addAlertFragmentViewModel: AddAlertFragmentViewModel
    private lateinit var addAlertFragmentViewModelFactory : AddAlertFragmentViewModelFactory

    var startDate:Long = 0
    var endDate:Long = 0
    lateinit var startDateSt:String
    lateinit var endDateSt:String

    var alertTime :Long = 0
    lateinit var alertType :String
    lateinit var alertDays: List<String>
    var lat: Double = 0.0
    var lon: Double = 0.0

    private lateinit var typeAlarm: RadioButton
    private lateinit var typeNotification: RadioButton
    private lateinit var radioGroupAlert: RadioGroup
    private lateinit var saveBtn: Button
    private lateinit var alarmFromEdit: TextInputLayout
    private lateinit var alarmToEdit: TextInputLayout
    private lateinit var alarmTimeEdit: TextInputLayout
    private lateinit var backBtn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        getSharedPreferences(this).apply {
            lat = getFloat(getString(R.string.lat), 0.0f).toDouble()
            lon = getFloat(getString(R.string.lon), 0.0f).toDouble()
        }
        initView()
        initViewModel()
        setListeners()
    }

    private fun setListeners() {
        saveBtn.setOnClickListener {
            getDataFromDialog()
        }
        backBtn.setOnClickListener {
            finish()
        }
        alarmTimeEdit.editText?.setOnClickListener(View.OnClickListener {
            openTimePicker()
        })

        alarmFromEdit.editText?.setOnClickListener(View.OnClickListener {
            openDatePicker("from")
        })
        alarmToEdit.editText?.setOnClickListener(View.OnClickListener {
            openDatePicker("to")
        })
    }
    private fun initViewModel(){
        addAlertFragmentViewModelFactory = AddAlertFragmentViewModelFactory( Repository.getInstance(
            RemoteSource.getInstance(), ConcreteLocalSource(this), this
        ))
        addAlertFragmentViewModel = ViewModelProvider(this,addAlertFragmentViewModelFactory)[AddAlertFragmentViewModel::class.java]

    }

    private fun initView(){
        backBtn = findViewById(R.id.backBtn)
        typeAlarm = findViewById<RadioButton>(R.id.alarm_radio)
        typeNotification = findViewById<RadioButton>(R.id.notification_radio)
        radioGroupAlert = findViewById<RadioGroup>(R.id.alarmSoundGroup)
        saveBtn = findViewById<Button>(R.id.addAlarm_btn)
        alarmFromEdit = findViewById(R.id.alarmFromEdit)
        alarmToEdit = findViewById(R.id.alarmToEdit)
        alarmTimeEdit = findViewById(R.id.alarmTimeEdit)

    }
    private fun openDatePicker(state: String) {
        val c = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                if (state == "from") {
                    startDateSt = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    Log.e("mando", "openDatePicker: "+startDateSt)
                    startDate = dateToLong(startDateSt)
                    alarmFromEdit.editText?.setText(startDateSt)
                } else {
                    endDateSt = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    endDate = dateToLong(endDateSt)
                    alarmToEdit.editText?.setText(endDateSt)
                }
            }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }


    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this,
            { viewTimePicker, hour, minute ->
                alertTime = timeToSeconds(hour, minute)
                alarmTimeEdit.editText?.setText(convertLongToTime(alertTime))

            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true
        )
        timePickerDialog.show()
    }

    private fun getDays(startDate: String, endDate: String): List<String> {
        val dtf = DateTimeFormat.forPattern("dd-MM-yyyy")
        val start = dtf.parseLocalDate(startDate)
        val end = dtf.parseLocalDate(endDate).plusDays(1)
        val myDays: MutableList<String> = ArrayList()
        val days = Days.daysBetween(LocalDate(start), LocalDate(end)).days.toLong()
        var i = 0
        while (i < days) {
            val current = start.plusDays(i)
            val date = current.toDateTimeAtStartOfDay().toString("dd-MM-yyyy")
            myDays.add(date)
            i++
        }
        return myDays
    }

    private fun getDataFromDialog(){
        if (typeAlarm.isChecked || typeNotification.isChecked && !alarmFromEdit.editText?.text.isNullOrBlank() && !alarmToEdit.editText?.text.isNullOrBlank() && !alarmTimeEdit.editText?.text.isNullOrBlank()){
            when (radioGroupAlert.checkedRadioButtonId) {
                R.id.notification_radio -> {
                    alertType = getString(R.string.notification)
                }
                R.id.alarm_radio -> {
                    alertType = getString(R.string.alarm)
                }
            }
            alertDays = getDays(startDateSt,endDateSt)
            addAlertFragmentViewModel.insertAlert(startDate,endDate, alertTime, alertType, alertDays, lat, lon)
            setPeriodWorkManger()
            finish()
//            addAlertDialog.dismiss()
        }
        else
        {
            Toast.makeText(this, "Please Fill Required Fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPeriodWorkManger() {

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            MyPeriodicManager::class.java, 24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "work", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
        Log.e("Create alarm","setPeriodWorkManger")

    }


}