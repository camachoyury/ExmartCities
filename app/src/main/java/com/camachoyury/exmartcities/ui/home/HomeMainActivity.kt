package com.camachoyury.exmartcities.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.camachoyury.exmartcities.ui.cities.CityListScreen
import com.camachoyury.exmartcities.ui.theme.ExmartCitiesTheme
import org.koin.androidx.compose.koinViewModel

class HomeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExmartCitiesTheme {
                CityListScreen(viewModel = koinViewModel())
            }
        }
    }
}