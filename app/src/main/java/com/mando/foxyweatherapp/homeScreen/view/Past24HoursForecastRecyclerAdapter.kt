package com.mando.foxyweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mando.foxyweatherapp.R
import com.mando.foxyweatherapp.model.responseModels.HourlyWeather
import com.mando.foxyweatherapp.utitlity.convertLongToTime
import com.mando.foxyweatherapp.utitlity.getDayOfWeek
import com.mando.foxyweatherapp.utitlity.getIcon

class Past24HoursForecastRecyclerAdapter :RecyclerView.Adapter<Past24HoursForecastRecyclerAdapter.ViewHolder>(){

    var hourlyWeather: List<HourlyWeather> = arrayListOf()

    inner class ViewHolder(private val itemView : View): RecyclerView.ViewHolder(itemView){

        val day : TextView = itemView.findViewById(R.id.time_tv)
        val temp : TextView = itemView.findViewById(R.id.temp_tv)
        val img : ImageView = itemView.findViewById(R.id.weather_condition_img)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_hourly_forecast,parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int = hourlyWeather.size

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        viewHolder.img.setImageResource(getIcon(hourlyWeather[position].weather[0].icon))
        viewHolder.day.text = convertLongToTime(hourlyWeather[position].dt)
        viewHolder.temp.text = "${hourlyWeather[position].temp.toInt()}°C"
 }
}