package com.gmail_bssushant2003.journeycraft.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LatLng(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
) : Serializable
