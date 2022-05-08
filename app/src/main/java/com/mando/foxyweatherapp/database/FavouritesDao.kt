package com.mando.foxyweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation
import com.mando.foxyweatherapp.model.responseModels.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {
    @Query("select * from favourites")
    fun getAllFavourites(): LiveData<List<FavouriteLocation>>

    @Insert(onConflict = REPLACE)
    fun insertFavourite(fav: FavouriteLocation)

    @Delete
    fun deleteFavourite(fav: FavouriteLocation)

}