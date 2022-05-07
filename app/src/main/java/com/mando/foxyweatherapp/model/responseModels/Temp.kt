package com.mando.foxyweatherapp.model.responseModels

import com.google.gson.annotations.SerializedName

data class Temp(
    @SerializedName("min") var min: Double,
    @SerializedName("max") var max: Double,
)
