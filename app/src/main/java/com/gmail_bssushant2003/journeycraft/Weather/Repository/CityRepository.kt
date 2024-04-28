package com.gmail_bssushant2003.weather.Repository

import com.gmail_bssushant2003.weather.Server.ApiServices

class CityRepository(val api:ApiServices){
    fun getCities(q:String,limit:Int)=
        api.getCitiesList(q,limit,"a071236763227fa496889868346d61c0")
}