package com.mando.foxyweatherapp.model.responseModels

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description") var description: String,
    @SerializedName("icon") var icon: String
)
