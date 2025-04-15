package com.camachoyury.exmartcities.data.repository

import com.camachoyury.exmartcities.data.City
import com.camachoyury.exmartcities.data.repository.CityRepository
import com.camachoyury.exmartcities.data.service.CityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class CityRepositoryImpl(val cityService: CityService): CityRepository {
    private val cityCache = mutableListOf<City>()
    private var isInitialized = false

    override suspend fun getCities(page: Int, pageSize: Int): List<City> {
        return withContext(Dispatchers.IO) {
            try {
                if (!isInitialized) {
                    cityCache.clear()
                    cityCache.addAll(cityService.getCities())
                    isInitialized = true
                }
                cityCache.drop(page * pageSize).take(pageSize)
            } catch (e: IOException) {
                println("Error al obtener las ciudades: ${e.message}")
                emptyList()
            } catch (e: Exception) {
                println("Error : ${e.message}")
                emptyList()
            }
        }
    }

    override fun resetCache() {
        isInitialized = false
        cityCache.clear()
    }
}