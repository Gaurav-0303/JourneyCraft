package com.gmail_bssushant2003.journeycraft.Transport

data class Airport(
    val business_id: String,
    val google_id: String,
    val place_id: String,
    val google_mid: String,
    val phone_number: String?,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val full_address: String,
    val review_count: Int,
    val rating: Double,
    val timezone: String,
    val working_hours: String?,
    val website: String?,
    val verified: Boolean,
    val place_link: String,
    val cid: String,
    val reviews_link: String,
    val owner_id: String,
    val owner_link: String,
    val owner_name: String,
    val booking_link: String?,
    val reservations_link: String?,
    val business_status: String,
    val type: String,
    val subtypes: List<String>,
    val photos_sample: List<Photo>,
    val reviews_per_rating: Map<String, Int>,
    val photo_count: Int,
    val about: About?,
    val address: String,
    val order_link: String?,
    val price_level: String?,
    val district: String?,
    val street_address: String?,
    val city: String,
    val zipcode: String?,
    val state: String,
    val country: String
)

data class Photo(
    val photo_id: String,
    val photo_url: String,
    val photo_url_large: String,
    val video_thumbnail_url: String?,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val photo_datetime_utc: String,
    val photo_timestamp: Long
)

data class About(
    val summary: String?,
    val details: Map<String, Map<String, Boolean>>
)

