package com.gmail_bssushant2003.weather.ViewModel

import androidx.lifecycle.ViewModel
import com.gmail_bssushant2003.weather.Repository.CityRepository
import com.gmail_bssushant2003.weather.Server.ApiClient
import com.gmail_bssushant2003.weather.Server.ApiServices

class CityViewModel(val repository: CityRepository) :ViewModel() {
    constructor():this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCity(q:String,limit:Int)=
        repository.getCities(q,limit)
}