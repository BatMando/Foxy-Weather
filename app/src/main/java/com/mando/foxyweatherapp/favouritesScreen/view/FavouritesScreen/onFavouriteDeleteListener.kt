package com.mando.foxyweatherapp.favouritesScreen.view.FavouritesScreen

import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation

interface onFavouriteDeleteListener {
    fun deleteFavourite(location: FavouriteLocation)
}