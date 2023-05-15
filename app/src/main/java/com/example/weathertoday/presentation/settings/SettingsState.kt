package com.example.weathertoday.presentation.settings


data class SettingsState(
    val tempUnit: String = "celsius",
    val windSpeed: String = "kmh",
    val tempUnitMenuState: Boolean = false,
    val windSpeedMenuState: Boolean = false

)