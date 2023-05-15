package com.example.weathertoday.presentation.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathertoday.presentation.home.Home
import com.example.weathertoday.presentation.home.HomeViewModel
import com.example.weathertoday.presentation.settings.Settings
import com.example.weathertoday.presentation.settings.SettingsViewModel

@Composable
fun SetupNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route,
        ) {
            val viewModel = hiltViewModel<HomeViewModel>()
            Home(
                viewModel.homeScreenState.value,
                navController
            )
        }

        composable(
            route = Screen.Settings.route,
        ) {
            val viewModel = hiltViewModel<SettingsViewModel>()

            Settings(
                navHostController = navController,
                state = viewModel.settingsScreenState.value,
                event = viewModel::onEvent

            )
        }

    }

}