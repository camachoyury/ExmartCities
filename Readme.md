# ExmartCities Project Documentation

## Project Description
ExmartCities is an application that allows users to search and explore cities. The application uses an API to fetch city data and displays it in a user-friendly interface.

## Technologies Used
- **Languages**: Kotlin, Java
- **Frameworks**: Android, Retrofit, Jetpack Compose
- **Dependency Manager**: Gradle

## Project Structure
The project is organized into the following packages:
- `com.camachoyury.exmartcities.data`: Contains data classes and repositories.
- `com.camachoyury.exmartcities.data.service`: Contains service interfaces for API communication.
- `com.camachoyury.exmartcities.ui`: Contains user interface-related classes.
- `com.camachoyury.exmartcities.ui.theme`: Contains theme and typography configurations.

## Main Classes

### `CityService`
Defines the interface for fetching city data from the API.

```kotlin
package com.camachoyury.exmartcities.data.service

import com.camachoyury.exmartcities.data.City
import retrofit2.http.GET

interface CityService {
    @GET("cities.json")
    suspend fun getCities(): List<City>
}
```

### `City`
Represents a city with its attributes.

```kotlin
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
```

### `CityRepositoryImpl`
Implements the repository to handle city data retrieval logic.

```kotlin
package com.camachoyury.exmartcities.data.repository

import com.camachoyury.exmartcities.data.City
import com.camachoyury.exmartcities.data.service.CityService

class CityRepositoryImpl(private val cityService: CityService) {
    private val cityCache = mutableListOf<City>()

    suspend fun getCities(page: Int, pageSize: Int): List<City> {
        return if (cityCache.isNotEmpty()) {
            cityCache
        } else {
            try {
                val cities = cityService.getCities()
                cityCache.addAll(cities)
                cities
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
```

### `ExmartCitiesTheme`
Defines the application's themes.

```kotlin
package com.camachoyury.exmartcities.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ExmartRed,
    onPrimary = ExmartWhite,
    primaryContainer = ExmartDarkRed,
    onPrimaryContainer = ExmartLightRed,
    secondary = ExmartGray,
    onSecondary = ExmartWhite,
    secondaryContainer = ExmartBlack,
    onSecondaryContainer = ExmartLightGray,
    background = ExmartBlack,
    onBackground = ExmartWhite,
    surface = ExmartBlack,
    onSurface = ExmartWhite,
    error = Color.Red,
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = ExmartRed,
    onPrimary = ExmartWhite,
    primaryContainer = ExmartLightRed,
    onPrimaryContainer = ExmartDarkRed,
    secondary = ExmartGray,
    onSecondary = ExmartWhite,
    secondaryContainer = ExmartLightGray,
    onSecondaryContainer = ExmartBlack,
    background = ExmartWhite,
    onBackground = ExmartBlack,
    surface = ExmartWhite,
    onSurface = ExmartBlack,
    error = Color.Red,
    onError = Color.White
)

@Composable
fun ExmartCitiesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

## Unit Testing
Unit tests are performed using JUnit and Mockito. Below is an example of a unit test for `CityRepositoryImpl`.

```kotlin
package com.camachoyury.exmartcities

import com.camachoyury.exmartcities.data.City
import com.camachoyury.exmartcities.data.Coord
import com.camachoyury.exmartcities.data.repository.CityRepositoryImpl
import com.camachoyury.exmartcities.data.service.CityService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

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
            City("City1", "Country1", id = 1, coord = Coord(1.0, 1.0), isFavorite = false),
            City("City2", "Country2", id = 2, coord = Coord(1.0, 1.0), isFavorite = false)
        )
    }

    @Test
    fun testGetCities() = runBlocking {
        `when`(mockService.getCities()).thenReturn(mockResponse)

        val cities = cityRepository.getCities(0, 10)
        assertEquals(mockResponse, cities)
    }
}
```

## Gradle Configuration
Ensure the following dependencies are included in your `build.gradle` file:

```groovy
dependencies {
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'androidx.compose.material3:material3:1.0.0'
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:3.11.2'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2'
}

android {
    packagingOptions {
        exclude 'META-INF/services/kotlinx.coroutines.CoroutineExceptionHandler'
    }
}
```

## Running the Application
To run the application, use Android Studio and select the emulator device `Pixel 9 API 35`. Ensure the emulator is properly configured and the API is available.

## Error Logging
To view error logs, open the Logcat panel in Android Studio and select the emulator device `Pixel 9 API 35`.

## Relevant Technical Decisions

1. **Use of Kotlin and Java**: Kotlin is chosen as the primary language due to its modern features and interoperability with Java. Kotlin offers a more concise and safer syntax, reducing the likelihood of errors.

2. **Repository-Based Architecture**: The implementation of `CityRepositoryImpl` follows the repository pattern, providing an abstraction over the data layer. This facilitates data management and allows changing the data source without affecting other parts of the code.

3. **Use of Retrofit for API Communication**: Retrofit is a robust library for making HTTP requests in Android. It simplifies the creation of RESTful services and the conversion of JSON responses to Kotlin objects.

4. **Jetpack Compose for User Interface**: Jetpack Compose is a modern toolkit for building user interfaces in Android. It allows creating UIs declaratively, simplifying development and maintenance.

5. **Theme Management with Material3**: The application uses Material3 to define light and dark themes, enhancing user experience and ensuring visual consistency.

## Recommended Patterns

1. **Repository Pattern**: This pattern is used to abstract the data access logic. In `CityRepositoryImpl`, it handles data retrieval from the API and caching.

    ```kotlin
    class CityRepositoryImpl(private val cityService: CityService) {
        private val cityCache = mutableListOf<City>()

        suspend fun getCities(page: Int, pageSize: Int): List<City> {
            return if (cityCache.isNotEmpty()) {
                cityCache
            } else {
                try {
                    val cities = cityService.getCities()
                    cityCache.addAll(cities)
                    cities
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }
    }
    ```

2. **Dependency Injection**: Use dependency injection to manage instances of `CityService` and `CityRepositoryImpl`. This facilitates unit testing and improves code modularity.

3. **Theme Design Pattern**: Define light and dark color schemes using Material3. This ensures the application looks good in different lighting conditions.

    ```kotlin
    private val DarkColorScheme = darkColorScheme(
        primary = ExmartRed,
        onPrimary = ExmartWhite,
        primaryContainer = ExmartDarkRed,
        onPrimaryContainer = ExmartLightRed,
        secondary = ExmartGray,
        onSecondary = ExmartWhite,
        secondaryContainer = ExmartBlack,
        onSecondaryContainer = ExmartLightGray,
        background = ExmartBlack,
        onBackground = ExmartWhite,
        surface = ExmartBlack,
        onSurface = ExmartWhite,
        error = Color.Red,
        onError = Color.Black
    )

    private val LightColorScheme = lightColorScheme(
        primary = ExmartRed,
        onPrimary = ExmartWhite,
        primaryContainer = ExmartLightRed,
        onPrimaryContainer = ExmartDarkRed,
        secondary = ExmartGray,
        onSecondary = ExmartWhite,
        secondaryContainer = ExmartLightGray,
        onSecondaryContainer = ExmartBlack,
        background = ExmartWhite,
        onBackground = ExmartBlack,
        surface = ExmartWhite,
        onSurface = ExmartBlack,
        error = Color.Red,
        onError = Color.White
    )
    ```

4. **Unit Testing with JUnit and Mockito**: Perform unit tests to ensure repository functionality and proper exception handling. Use Mockito to mock service responses.

    ```kotlin
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
                City("City1", "Country1", id = 1, coord = Coord(1.0, 1.0), isFavorite = false),
                City("City2", "Country2", id = 2, coord = Coord(1.0, 1.0), isFavorite = false)
            )
        }

        @Test
        fun testGetCities() = runBlocking {
            `when`(mockService.getCities()).thenReturn(mockResponse)

            val cities = cityRepository.getCities(0, 10)
            assertEquals(mockResponse, cities)
        }
    }
    ```

These decisions and patterns ensure that the project is maintainable, scalable, and easy to test.
---
### Relevant Technical Decisions

1. **Use of Kotlin and Java**: Kotlin is chosen as the primary language due to its modern features and interoperability with Java. Kotlin offers a more concise and safer syntax, reducing the likelihood of errors.

2. **Repository-Based Architecture**: The implementation of `CityRepositoryImpl` follows the repository pattern, providing an abstraction over the data layer. This facilitates data management and allows changing the data source without affecting other parts of the code.

3. **Use of Retrofit for API Communication**: Retrofit is a robust library for making HTTP requests in Android. It simplifies the creation of RESTful services and the conversion of JSON responses to Kotlin objects.

4. **Jetpack Compose for User Interface**: Jetpack Compose is a modern toolkit for building user interfaces in Android. It allows creating UIs declaratively, simplifying development and maintenance.

5. **Theme Management with Material3**: The application uses Material3 to define light and dark themes, enhancing user experience and ensuring visual consistency.


### Suggestions for Refactoring and Improving App Development

#### 1. Use of `sealed class` for State Management
Instead of using multiple state variables (`loading`, `error`, etc.), you can use a `sealed class` to handle different UI states more cleanly and scalably.

```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

#### 2. Dependency Injection with Koin
Ensure all dependencies are properly injected using Koin to improve modularity and testability of your application.

```kotlin
val appModule = module {
    single { Retrofit.Builder().baseUrl(BASE_URL).build().create(CityService::class.java) }
    single { CityRepositoryImpl(get()) }
    viewModelOf(::CitiesViewModel)
}
```

#### 3. Use of `StateFlow` instead of `LiveData`
`StateFlow` is a modern alternative to `LiveData` that offers better state management and is more suitable for reactive programming.

```kotlin
class CitiesViewModel(private val cityRepository: CityRepositoryImpl) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<City>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<City>>> = _uiState

    fun fetchCities() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val cities = cityRepository.getCities()
                _uiState.value = UiState.Success(cities)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

#### 4. Use of `rememberSaveable` to Maintain State
Use `rememberSaveable` to preserve UI state across configuration changes, such as screen rotations.

```kotlin
var searchQuery by rememberSaveable { mutableStateOf("") }
```

#### 5. Separation of Responsibilities
Divide responsibilities into smaller, reusable components. For example, separate the search logic into a separate component.

```kotlin
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
```

#### 6. Use of `LazyColumn` with `itemsIndexed`
To better handle loading more items at the end of the list, use `itemsIndexed` instead of `items`.

```kotlin
LazyColumn(state = listState) {
    itemsIndexed(cities) { index, city ->
        CityItem(city, viewModel)
        if (index == cities.size - 1 && !isLoadingMore && !loading) {
            LaunchedEffect(Unit) {
                isLoadingMore = true
                viewModel.fetchCities()
                isLoadingMore = false
            }
        }
    }
}
```

Implementing these improvements can help keep your code cleaner, more modular, and easier to maintain.