package com.example.weathertoday.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertoday.data.data_source.local.Settings
import com.example.weathertoday.domain.location.LocationTracker
import com.example.weathertoday.domain.repository.WeatherRepository
import com.example.weathertoday.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val preferences: Settings
) : ViewModel() {

    private val _homeScreenState = mutableStateOf(HomeState())
    val homeScreenState: State<HomeState> = _homeScreenState

    init {

        viewModelScope.launch {
            _homeScreenState.value = homeScreenState.value.copy(
                isLoading = true, error = null
            )


           viewModelScope.launch {
               preferences.windSpeed.collect { windSpeed ->
                   _homeScreenState.value = homeScreenState.value.copy(
                       windSpeed = windSpeed
                   )
                   locationTracker.getCurrentLocation()?.let { location ->

                       when (val result = repository.getWeatherData(
                           location.latitude, location.longitude, windSpeed, _homeScreenState.value.tempUnit
                       )) {
                           is Resource.Success -> {
                               _homeScreenState.value = homeScreenState.value.copy(
                                   weatherInfo = result.data,
                                   isLoading = false,
                                   error = null,
                                   updateTime = System.currentTimeMillis()
                               )


                           }

                           is Resource.Error -> {
                               _homeScreenState.value = homeScreenState.value.copy(
                                   weatherInfo = null, isLoading = false, error = result.message
                               )


                           }

                       }
                   } ?: kotlin.run {
                       _homeScreenState.value = homeScreenState.value.copy(
                           isLoading = false,
                           error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                       )
                   }

               }
           }

            viewModelScope.launch {

                    preferences.temperatureUnit.collect { temperature ->
                        _homeScreenState.value = homeScreenState.value.copy(
                            tempUnit = temperature
                        )
                        locationTracker.getCurrentLocation()?.let { location ->

                            when (val result = repository.getWeatherData(
                                location.latitude, location.longitude, _homeScreenState.value.windSpeed, temperature
                            )) {
                                is Resource.Success -> {
                                    _homeScreenState.value = homeScreenState.value.copy(
                                        weatherInfo = result.data,
                                        isLoading = false,
                                        error = null,
                                        updateTime = System.currentTimeMillis()
                                    )


                                }

                                is Resource.Error -> {
                                    _homeScreenState.value = homeScreenState.value.copy(
                                        weatherInfo = null, isLoading = false, error = result.message
                                    )


                                }

                            }
                        } ?: kotlin.run {
                            _homeScreenState.value = homeScreenState.value.copy(
                                isLoading = false,
                                error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                            )
                        }

                    }



            }




        }


    }


    fun load(temperature: String) {

    }

}