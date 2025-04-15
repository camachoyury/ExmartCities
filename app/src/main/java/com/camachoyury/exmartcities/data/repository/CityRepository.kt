package com.camachoyury.exmartcities.data.repository

import com.camachoyury.exmartcities.data.City

interface CityRepository {
   suspend fun getCities(page: Int, pageSize: Int): List<City>
   fun resetCache()
}