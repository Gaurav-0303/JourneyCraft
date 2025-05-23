package com.gmail_bssushant2003.weather.Server

import com.gmail_bssushant2003.weather.model.CityResponseApi
import retrofit2.Call
import com.gmail_bssushant2003.weather.model.CurrentResponseApi
import com.gmail_bssushant2003.weather.model.ForecastResponseApi
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") APIKey:String,
    ):Call<CurrentResponseApi>


    @GET("data/2.5/forecast")
    fun getWeatherForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") APIKey:String,
    ):Call<ForecastResponseApi>

    @GET("geo/1.0/direct")
    fun getCitiesList(
        @Query("q") q:String,
        @Query("limit") limit:Int,
        @Query("apiid") APIKey: String
    ):Call<CityResponseApi>
}