package com.example.weathertoday.presentation.util

sealed class Screen(val route: String) {
    object HomeScreen : Screen(route = "home")
    object Settings : Screen(route = "settings")
}