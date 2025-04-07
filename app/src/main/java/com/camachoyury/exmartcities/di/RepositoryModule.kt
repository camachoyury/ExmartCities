package com.camachoyury.exmartcities.di

import com.camachoyury.exmartcities.data.repository.CityRepository
import com.camachoyury.exmartcities.data.repository.CityRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<CityRepository>  { CityRepositoryImpl(get()) }
}