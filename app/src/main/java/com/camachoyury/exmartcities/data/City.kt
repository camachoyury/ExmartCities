package com.camachoyury.exmartcities.data

data class City(
    val country: String,
    val name: String,
    val id: Int,
    val coord: Coord,
    var isFavorite: Boolean = false

)

data class Coord(
    val lon: Double,
    val lat: Double
)