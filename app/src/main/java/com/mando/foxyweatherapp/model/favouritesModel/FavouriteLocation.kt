package com.mando.foxyweatherapp.model.favouritesModel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteLocation(var lat: Double,
                             var lon: Double,
                             @PrimaryKey
                             var locationName:String
)
