package com.gmail_bssushant2003.journeycraft.Models

import java.io.Serializable

data class Preferences(
    val isLiftedTemple : Boolean,
    var isLiftedBeach : Boolean,
    var isLiftedHistorical : Boolean,
    var isLiftedMountain : Boolean,
    var isLiftedLake : Boolean,
    var isLiftedMuseum : Boolean,
    var isLiftedPark : Boolean,
    var isLiftedWildlife : Boolean
) : Serializable


