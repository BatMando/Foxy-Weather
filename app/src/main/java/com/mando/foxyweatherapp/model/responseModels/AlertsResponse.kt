package com.mando.foxyweatherapp.model.responseModels

import com.google.gson.annotations.SerializedName

data class AlertsResponse(
    @SerializedName("sender_name") var senderName: String? = null,
    @SerializedName("event") var event: String? = null,
    @SerializedName("start") var start: Long? = null,
    @SerializedName("end") var end: Long? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("tags") var tags: List<String>)
