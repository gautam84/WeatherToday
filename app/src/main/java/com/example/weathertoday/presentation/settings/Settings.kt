package com.example.weathertoday.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Settings(
    navHostController: NavHostController, state: SettingsState, event: (SettingsEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                navHostController.navigateUp()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")

            }


        }


        Column(modifier = Modifier
            .clickable {
                event(SettingsEvent.DismissTempUnitMenu)
            }
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(text = "Temperature", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = if (state.tempUnit == "celsius") "Celsius -°C" else "Fahrenheit -°F",
                style = MaterialTheme.typography.labelMedium
            )

            DropdownMenu(expanded = state.tempUnitMenuState,
                onDismissRequest = { event(SettingsEvent.SetTempUnitMenuToFalse) }) {
                DropdownMenuItem(text = { Text(text = "Celsius -°C") }, onClick = {
                    event(SettingsEvent.ChangeTemp("celsius"))
                })
                DropdownMenuItem(text = { Text(text = "Fahrenheit -°F") }, onClick = {
                    event(SettingsEvent.ChangeTemp("fahrenheit"))
                })

            }

        }


        Column(modifier = Modifier
            .clickable {
                event(SettingsEvent.DismissWindSpeedMenu)
            }
            .fillMaxWidth()
            .padding(16.dp)) {

            Text(text = "Wind", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = getWindSpeedLabel(state.windSpeed),
                style = MaterialTheme.typography.labelMedium
            )

            val list = listOf("kmh", "mph","ms","kn")

            DropdownMenu(expanded = state.windSpeedMenuState, onDismissRequest = {
                event(SettingsEvent.SetWindSpeedMenuToFalse)
            }) {
                list.forEach{ tempUnit ->
                    DropdownMenuItem(text = { Text(text = getWindSpeedLabel(tempUnit)) }, onClick = { event(SettingsEvent.ChangeWindSpeed(tempUnit))})

                }

            }

        }


    }

}

fun getWindSpeedLabel(windSpeed: String): String {

    return when (windSpeed) {
        "mph" -> "Miles Per Hour -mph"
        "kmh" -> "Kilometer Per Hour -kmh"
        "kn" -> "Knots"
        "ms" -> "Meter Per Second -ms"
        else -> "Kilometer Per Hour -kmh"
    }

}
