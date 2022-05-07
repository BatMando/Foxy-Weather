package com.mando.foxyweatherapp.favouritesScreen.view

import com.mando.foxyweatherapp.model.favouritesModel.FavouriteLocation

interface onFavouriteClickListener {
    fun openFavourite(location: FavouriteLocation)
}