package com.camachoyury.exmartcities.di

import com.camachoyury.exmartcities.ui.cities.CitiesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule= module {
    viewModelOf(::CitiesViewModel)
}