package com.example.weathertoday.presentation.home

import com.example.weathertoday.domain.model.WeatherInfo

data class HomeState(
    val currPlace: String = "",
    val updateTime: Long = 0,
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val tempUnit: String = "celsius",
    val windSpeed: String = "kmh"
)
