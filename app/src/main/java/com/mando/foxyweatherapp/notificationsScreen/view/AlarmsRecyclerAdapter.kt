package com.mando.foxyweatherapp.notificationsScreen.view

import android.annotation.SuppressLint
import android.graphics.Insets.add
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.DailyWeather
import com.mando.foxyweatherapp.utitlity.*

class AlarmsRecyclerAdapter( private val alarmDeleteListener: onAlarmDeleteListener) :RecyclerView.Adapter<AlarmsRecyclerAdapter.ViewHolder>(){

    var alerts: List<Alerts> = arrayListOf()

    inner class ViewHolder(private val itemView : View): RecyclerView.ViewHolder(itemView) ,
        OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        val startDate : TextView = itemView.findViewById(R.id.start_date_tv)
        val endDate : TextView = itemView.findViewById(R.id.end_date_tv)
        val startTime : TextView = itemView.findViewById(R.id.start_time_tv)
        val endTime : TextView = itemView.findViewById(R.id.end_time_tv)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.alerts_constraint)

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.setHeaderTitle("Choose")
            val delete: MenuItem = menu!!.add(Menu.NONE, 1, 1, "Delete")
            delete.setOnMenuItemClickListener(this)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            if (alarmDeleteListener != null) {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    when (item?.itemId) {
                        1 -> {
                            alarmDeleteListener.deleteAlarm(alerts[pos])
                            return true
                        }
                    }
                }
            }
            return false
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_alerts,parent,false)
        return ViewHolder(view)
    }



    override fun getItemCount(): Int = alerts.size

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        viewHolder.startDate.text = longToDateAsString(alerts[position].startDate)
        viewHolder.startTime.text = convertLongToTime(alerts[position].startDate)
        viewHolder.endDate.text = longToDateAsString(alerts[position].endDate)
        viewHolder.endTime.text = convertLongToTime(alerts[position].endDate)
        viewHolder.constraintLayout.setOnCreateContextMenuListener(viewHolder)
    }
}

