package com.mando.foxyweatherapp.alertsScreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.utitlity.*

class AlertsRecyclerAdapter(private val alertDeleteListener: onAlertDeleteListener) :RecyclerView.Adapter<AlertsRecyclerAdapter.ViewHolder>(){

    var alerts: List<Alerts> = arrayListOf()
    private var language: String = "en"


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
            if (alertDeleteListener != null) {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    when (item?.itemId) {
                        1 -> {
                            alertDeleteListener.deleteAlert(alerts[pos])
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


    fun setValuesFromSharedPreferences(context: Context) {
        getSharedPreferences(context).apply {
            language = getString(context.getString(R.string.languageSetting), "en") ?: "en"
        }
    }

    override fun getItemCount(): Int = alerts.size

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        viewHolder.startDate.text = longToDateAsString(alerts[position].startDate,language)
        viewHolder.startTime.text = convertLongToTime(alerts[position].alertTime,language)
        viewHolder.endDate.text = longToDateAsString(alerts[position].endDate,language)
        viewHolder.endTime.text = convertLongToTime(alerts[position].alertTime,language)
        viewHolder.constraintLayout.setOnCreateContextMenuListener(viewHolder)
    }
}

