package com.camachoyury.exmartcities.data.repository

import com.camachoyury.exmartcities.data.City
import com.camachoyury.exmartcities.data.repository.CityRepository
import com.camachoyury.exmartcities.data.service.CityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class CityRepositoryImpl(val cityService: CityService): CityRepository {

    val cityCache = mutableListOf<City>()

    override suspend fun getCities(page: Int, pageSize: Int): List<City> {
        return withContext(Dispatchers.IO) {
            if (cityCache.isEmpty()) {
                try {
                    val allCities = cityService.getCities()
                    cityCache.addAll(allCities)
                } catch (e: IOException) {
                    println("Error al obtener las ciudades: ${e.message}")
                    return@withContext emptyList<City>()
                } catch (e: Exception) {
                    println("Error : ${e.message}")
                    return@withContext emptyList<City>()
                }
            }
            cityCache.drop(page * pageSize).take(pageSize)
        }
    }

}