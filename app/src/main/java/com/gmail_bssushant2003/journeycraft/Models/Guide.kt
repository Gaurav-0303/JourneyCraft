package com.gmail_bssushant2003.journeycraft.Models

import java.io.Serializable

data class Guide(
    val id: Long? = null,
    val user: User? = null,
    val name: String? = null,
    val experience: Int = 0,
    val language: String? = null,
    val bio: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val licenseNumber: String? = null,
    val isAvailable: Boolean? = null,
    val phoneNo: String? = null
) : Serializable
