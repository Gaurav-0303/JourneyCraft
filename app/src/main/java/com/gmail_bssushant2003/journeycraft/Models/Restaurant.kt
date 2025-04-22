package com.gmail_bssushant2003.journeycraft.Models

import java.time.LocalTime

data class Restaurant(
    val id: Long? = null,
    val user: User? = null,
    val name: String? = null,
    val rating: Double? = null,
    val locationLink: String? = null,
    val fssaiLicense: String? = null,
    val openTime: String? = null,
    val closeTime: String? = null,
    val description: String? = null,
    val phoneNo: String? = null,
    val averageCost: Double? = null,
    val foodType: FoodType? = null
) {
    enum class FoodType {
        VEG, NON_VEG, BOTH
    }
}
