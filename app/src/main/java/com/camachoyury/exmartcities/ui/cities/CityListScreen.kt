package com.camachoyury.exmartcities.ui.cities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.camachoyury.exmartcities.R
import com.camachoyury.exmartcities.data.City
import com.camachoyury.exmartcities.ui.cities.CitiesViewModel
import com.camachoyury.exmartcities.ui.theme.ExmartWhite
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(viewModel: CitiesViewModel) {
    val navController = rememberNavController()
    val cities by viewModel.filteredCities.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    var isLoadingMore by remember { mutableStateOf(false) }
    var isMapScreen by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { index ->
                if (index == cities.size - 1 && !isLoadingMore && !loading && searchQuery.isEmpty() && viewModel.hasMore.value) {
                    isLoadingMore = true
                    viewModel.fetchCities()
                    isLoadingMore = false
                }
            }
    }
    Scaffold(
        topBar = {
            Column {
                TabRow(selectedTabIndex = 0) {
                    Tab(selected = true, onClick = {}) {
                        Text("City List", modifier = Modifier.padding(16.dp))
                    }
                }
                Row(
                    modifier = Modifier
                        .background(ExmartWhite)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchCities(it)

                        },
                        label = { Text("Search") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search Icon")
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )
                    IconButton(onClick = {
                        isMapScreen = !isMapScreen
                        if (isMapScreen) {
                            navController.navigate("map")
                        } else {
                            navController.navigate("list")
                        }
                    }) {
                        Image(
                            painter = painterResource(if (isMapScreen){ R.drawable.list} else R.drawable.map),
                            contentDescription = if (isMapScreen) "Show List" else "Show Map",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = "list") {
            composable("list") {
                isMapScreen = false
                LazyColumn(
                    state = listState,
                    modifier = Modifier.padding(padding)
                        .fillMaxSize()
                ) {
                    items(cities) { city ->
                        CityItem(city, viewModel)
                    }

                    if (loading && cities.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    if (error.isNotEmpty()) {
                        item {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
            composable("map") {
                isMapScreen = true
                MapScreen(cities)
            }
        }
    }
}

@Composable
fun CityItem(city: City, viewModel: CitiesViewModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .shadow(4.dp)
            .clickable {
                //TODO action when clicked
            }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(ExmartWhite)
                .clickable {
                    //TODO action when clicked
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.smart_city), // Replace with actual icon
                contentDescription = city.name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = city.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Country: ${city.country},  Lat: ${city.coord.lat}, Lon: ${city.coord.lon}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(
                imageVector = if (city.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (city.isFavorite) "Unfavorite" else "Favorite",
                modifier = Modifier
                    .size(24.dp)

            )
        }
    }
}
