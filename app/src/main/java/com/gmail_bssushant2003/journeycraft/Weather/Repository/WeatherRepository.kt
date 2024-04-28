package com.gmail_bssushant2003.weather.Repository

import com.gmail_bssushant2003.weather.Server.ApiServices

class WeatherRepository(val api:ApiServices) {
    fun getCurrentWeather(lat: Double, lng : Double, unit : String) =
        api.getCurrentWeather(lat,lng,unit,"a071236763227fa496889868346d61c0")

    fun getForecastWeather(lat: Double, lng : Double, unit : String) =
        api.getWeatherForecast(lat,lng,unit,"a071236763227fa496889868346d61c0")
}