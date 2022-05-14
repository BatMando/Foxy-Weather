package com.mando.foxyweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.responseModels.DailyWeather
import com.mando.foxyweatherapp.utitlity.getDayOfWeek
import com.mando.foxyweatherapp.utitlity.getIcon
import com.mando.foxyweatherapp.utitlity.getSharedPreferences

class Past7DaysForecastRecyclerAdapter :RecyclerView.Adapter<Past7DaysForecastRecyclerAdapter.ViewHolder>(){
    private var language: String = "en"
    private var units: String = "metric"

    var dailyWeather: List<DailyWeather> = arrayListOf()

    inner class ViewHolder(private val itemView : View): RecyclerView.ViewHolder(itemView){

        val day : TextView = itemView.findViewById(R.id.day_tv)
        val temp : TextView = itemView.findViewById(R.id.temp_tv)
        val img : ImageView = itemView.findViewById(R.id.weather_condition_img)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_daily_forecast,parent,false)
        return ViewHolder(view)
    }

    fun setValuesFromSharedPreferences(context:Context) {
        getSharedPreferences(context).apply {
            language = getString(context.getString(R.string.languageSetting), "en") ?: "en"
            units = getString(context.getString(R.string.unitsSetting), "metric") ?: "metric"
        }
    }


    override fun getItemCount(): Int = dailyWeather.size

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        viewHolder.img.setImageResource(getIcon(dailyWeather[position].weather[0].icon))
        viewHolder.day.text = getDayOfWeek(dailyWeather[position].dt,language)
        viewHolder.temp.text = "${dailyWeather[position].temp.max.toInt()}°C/${dailyWeather[position].temp.min.toInt()}°C"
 }
}