package com.mando.foxyweatherapp.alertsScreen.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesappmvvm.model.Repository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.mando.foxyweatherapp.MainActivity
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.addalarmscreen.view.AddAlarmActivity
import com.mando.foxyweatherapp.alertsScreen.viewModel.AlertsFragmentViewModel
import com.mando.foxyweatherapp.alertsScreen.viewModel.AlertsFragmentViewModelFactory
import com.mando.foxyweatherapp.database.ConcreteLocalSource
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.network.RemoteSource
import com.mando.foxyweatherapp.utitlity.convertLongToTime
import com.mando.foxyweatherapp.utitlity.dateToLong
import com.mando.foxyweatherapp.utitlity.getSharedPreferences
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.*

class AlertsFragment : Fragment() , onAlertDeleteListener{

    private lateinit var alarmsRecyclerAdapter: AlertsRecyclerAdapter
    private lateinit var alarmsLayoutManger : RecyclerView.LayoutManager
    private lateinit var alarmsRecyclerView: RecyclerView
    private lateinit var alertViewModel: AlertsFragmentViewModel
    private lateinit var alertFactory : AlertsFragmentViewModelFactory
    private lateinit var addAlert: FloatingActionButton


    lateinit var addAlertDialog: Dialog




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setListeners()
        initViewModel()
        observeAlerts()
    }

    private fun observeAlerts(){
        alertViewModel.getAlertsFromDatabase().observe(this){
            if (it !=null)
                alarmsRecyclerAdapter.alerts = it
            alarmsRecyclerAdapter.setValuesFromSharedPreferences(requireContext())
            alarmsRecyclerAdapter.notifyDataSetChanged()
        }
    }
    private fun initView(view: View){

        addAlertDialog = Dialog(requireContext())
        addAlert = view.findViewById(R.id.add_alarm)
        alarmsRecyclerView = view.findViewById(R.id.alarms_recycler)
        alarmsRecyclerAdapter = AlertsRecyclerAdapter(this)
        alarmsLayoutManger = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        alarmsRecyclerView.layoutManager = alarmsLayoutManger
        alarmsRecyclerView.adapter = alarmsRecyclerAdapter
    }
    private fun initViewModel(){
        alertFactory = AlertsFragmentViewModelFactory( Repository.getInstance(
            RemoteSource.getInstance(), ConcreteLocalSource(requireContext()), requireContext()
        ))
        alertViewModel = ViewModelProvider(this,alertFactory)[AlertsFragmentViewModel::class.java]

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners(){
        addAlert.setOnClickListener {
            openAddAlertActivity()
        }
    }

    override fun deleteAlert(alerts: Alerts) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.deleteConfirmation))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                alertViewModel.deleteAlert(alerts)
                dialog.dismiss()
                Toast.makeText(requireContext(),getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show()
            }
            .show()
    }
    private fun checkFirstTime(): Boolean {
        return getSharedPreferences(requireContext()).getBoolean(getString(R.string.permission), true)
    }

    private fun setNotFirstTime() {
        getSharedPreferences(requireContext()).edit().putBoolean(getString(R.string.permission), false).apply()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun openAddAlertActivity() {
        checkDrawOverlayPermission()
        if (checkFirstTime()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkDrawOverlayPermission()
                setNotFirstTime()
            } else {
                val intent = Intent(requireContext(), AddAlarmActivity::class.java)
                startActivity(intent)
            }
        } else {
            val intent = Intent(requireContext(), AddAlarmActivity::class.java)
            startActivity(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkDrawOverlayPermission() {
        // Check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(requireContext())) {
            // if not construct intent to request permission
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.overlay_title))
                .setMessage(getString(R.string.overlay_message))
                .setPositiveButton(getString(R.string.overlay_postive_button)) { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().packageName)
                    )
                    dialog.dismiss()
                    // request permission via start activity for result
                    startActivityForResult(intent, 1)
                    //It will call onActivityResult Function After you press Yes/No and go Back after giving permission


                }.setNegativeButton(
                    getString(R.string.overlay_negative_button)
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }


}