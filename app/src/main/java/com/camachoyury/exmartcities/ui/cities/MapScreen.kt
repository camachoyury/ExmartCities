package com.camachoyury.exmartcities.ui.cities

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.camachoyury.exmartcities.data.City
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker

import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(cities: List<City>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 2f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        cities.forEach { city ->
            val position = LatLng(city.coord.lat, city.coord.lon)
            Marker(
                position = position,
                title = city.name,
                snippet = city.country,
            )
        }
        if (cities.isNotEmpty()) {
            val firstCity = cities[0]
            val firstPosition = LatLng(firstCity.coord.lat, firstCity.coord.lon)
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(firstPosition, 10f))
        }
    }
}