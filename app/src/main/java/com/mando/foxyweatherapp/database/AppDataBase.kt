package com.mando.foxyweatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mando.foxyweatherapp.model.alertsModel.Alerts
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse

@TypeConverters(Converters::class)
@Database(entities = [WeatherResponse::class,FavouriteLocation::class,Alerts::class], version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun alertsDao(): AlertsDao
    abstract fun favouritesDao(): FavouritesDao

    companion object {
        private var instance: AppDataBase? = null
        @Synchronized
        fun getInstance(context: Context): AppDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java, "Weather"
                ).build()
            }
            return instance
        }
    }
}