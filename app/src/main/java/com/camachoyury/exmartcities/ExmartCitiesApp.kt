package com.camachoyury.exmartcities

import android.app.Application
import com.camachoyury.exmartcities.di.networkModule
import com.camachoyury.exmartcities.di.repositoryModule
import com.camachoyury.exmartcities.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ExmartcitiesApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ExmartcitiesApp)
            modules(viewModelModule, repositoryModule, networkModule)
        }
    }
}