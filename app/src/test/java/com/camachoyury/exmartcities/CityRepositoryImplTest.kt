package com.camachoyury.exmartcities

import com.camachoyury.exmartcities.data.City
import com.camachoyury.exmartcities.data.Coord
import com.camachoyury.exmartcities.data.repository.CityRepositoryImpl
import com.camachoyury.exmartcities.data.service.CityService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.io.IOException

@ExperimentalCoroutinesApi
class CityRepositoryImplTest {

    private lateinit var mockService: CityService
    private lateinit var cityRepository: CityRepositoryImpl
    private lateinit var mockResponse: List<City>

    @Before
    fun setUp() {
        mockService = mock(CityService::class.java)
        cityRepository = CityRepositoryImpl(mockService)
        mockResponse = listOf(
            City(
                "City1", "Country1",
                id = 1,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            ),
            City(
                "City2", "Country2",
                id = 2,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            )
        )
    }

    @Test
    fun testGetCities() = runBlocking {

        Mockito.`when`(mockService.getCities()).thenReturn(mockResponse)

        val repository = CityRepositoryImpl(mockService)
        val cities = repository.getCities(0, 10)
        Assert.assertEquals(
            listOf(
                City(
                    "City1", "Country1",
                    id = 1,
                    coord = Coord(1.0, 1.0),
                    isFavorite = false
                ),
                City(
                    "City2", "Country2",
                    id = 2,
                    coord = Coord(1.0, 1.0),
                    isFavorite = false
                )
            ), cities
        )
    }

    @Test
    fun testGetBreedsPagination() = runBlocking {
        mockResponse = listOf(
            City(
                "City1", "Country1",
                id = 1,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            ),
            City(
                "City2", "Country2",
                id = 2,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            ),
            City(
                "City3", "Country3",
                id = 3,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            ),
            City(
                "City4", "Country4",
                id = 4,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            )
        )
        Mockito.`when`(mockService.getCities()).thenReturn(mockResponse)

        val repository = CityRepositoryImpl(mockService)
        val breedsPage1 = repository.getCities(0, 2)
        val breedsPage2 = repository.getCities(1, 2)
        Assert.assertEquals(listOf(
            City(
                "City1", "Country1",
                id = 1,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            ),
            City(
                "City2", "Country2",
                id = 2,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            )), breedsPage1)


        Assert.assertEquals(listOf(
            City(
                "City3", "Country3",
                id = 3,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            ),
            City(
                "City4", "Country4",
                id = 4,
                coord = Coord(1.0, 1.0),
                isFavorite = false
            )), breedsPage2)
    }

    @Test
    fun testGetCitiesHandlesIOException() = runBlocking {
        `when`(mockService.getCities()).thenThrow(RuntimeException("Network error"))

        val result = cityRepository.getCities(0, 10)
        assertEquals(emptyList<City>(), result)
    }

    @Test
    fun testGetCitiesHandlesGenericException() = runBlocking {
        `when`(mockService.getCities()).thenThrow(RuntimeException("Generic error"))

        val result = cityRepository.getCities(0, 10)
        assertEquals(emptyList<City>(), result)
    }

    @Test
    fun testGetCitiesFromCache() = runBlocking {
        `when`(mockService.getCities()).thenReturn(mockResponse)

        // First call to populate cache
        cityRepository.getCities(0, 10)
        // Second call should use cache
        val result = cityRepository.getCities(0, 10)

        verify(mockService, times(1)).getCities()
        assertEquals(mockResponse, result)
    }

    @Test
    fun testGetCitiesEmptyList() = runBlocking {
        `when`(mockService.getCities()).thenReturn(emptyList())

        val result = cityRepository.getCities(0, 10)
        assertEquals(emptyList<City>(), result)
    }

}