package com.camachoyury.exmartcities.data.service

import com.camachoyury.exmartcities.data.City
import retrofit2.http.GET

interface CityService {
    @GET("cities.json")
    suspend fun getCities(): List<City>


}