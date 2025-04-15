package com.camachoyury.exmartcities.ui.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camachoyury.exmartcities.data.City
import com.camachoyury.exmartcities.data.repository.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CitiesViewModel(private val repository: CityRepository) : ViewModel() {
    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _filteredCities = MutableStateFlow<List<City>>(emptyList())
    val filteredCities: StateFlow<List<City>> = _filteredCities.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore

    private var currentPage = 0
    private val pageSize = 20

    init {
        fetchCities()
    }

    fun fetchCities() {
        if (!_hasMore.value || _loading.value) return
        
        viewModelScope.launch {
            _loading.value = true
            try {
                val newCities = repository.getCities(currentPage, pageSize)
                if (newCities.isEmpty()) {
                    _hasMore.value = false
                } else {
                    _cities.value = _cities.value + newCities
                    _filteredCities.value = _cities.value
                    _error.value = ""
                    currentPage++
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun searchCities(query: String) {
        viewModelScope.launch {
            _filteredCities.value = if (query.isEmpty()) {
                _cities.value
            } else {
                _cities.value.filter { it.name.startsWith(query, ignoreCase = true) }
            }
        }
    }

    fun resetPagination() {
        currentPage = 0
        _cities.value = emptyList()
        _filteredCities.value = emptyList()
        _hasMore.value = true
        repository.resetCache()
        fetchCities()
    }
}