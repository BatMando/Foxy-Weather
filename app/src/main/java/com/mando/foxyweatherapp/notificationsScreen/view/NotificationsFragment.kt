package com.mando.foxyweatherapp.notificationsScreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.alertsModel.Alerts

class NotificationsFragment : Fragment() , onAlarmDeleteListener{

    private lateinit var alarmsRecyclerAdapter: AlarmsRecyclerAdapter
    private lateinit var alarmsLayoutManger : RecyclerView.LayoutManager
    private lateinit var alarmsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view)

    }
    private fun initRecycler(view: View){

        var alerts : List<Alerts> = listOf(Alerts(1651759701,1652000400,1),Alerts(1651759200,1652139600,1),Alerts(1651759701,1652269740,1))
        alarmsRecyclerView = view.findViewById(R.id.alarms_recycler)
        alarmsRecyclerAdapter = AlarmsRecyclerAdapter(this)
        alarmsLayoutManger = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        alarmsRecyclerView.layoutManager = alarmsLayoutManger
        alarmsRecyclerView.adapter = alarmsRecyclerAdapter
        alarmsRecyclerAdapter.alerts = alerts
        alarmsRecyclerAdapter.notifyDataSetChanged()

    }

    override fun deleteAlarm(alerts: Alerts) {
        TODO("Not yet implemented")
    }


}