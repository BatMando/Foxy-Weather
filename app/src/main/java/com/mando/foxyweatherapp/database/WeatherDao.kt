package com.mando.foxyweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from weather where id = 0")
    suspend fun getAllWeather(): WeatherResponse

    @Insert(onConflict = REPLACE)
    suspend fun insertWeather(weather: WeatherResponse)
}