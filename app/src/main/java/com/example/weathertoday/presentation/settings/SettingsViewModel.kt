package com.example.weathertoday.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertoday.data.data_source.local.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: Settings
) : ViewModel() {

    private val _settingsScreenState = mutableStateOf(SettingsState())
    val settingsScreenState: State<SettingsState> = _settingsScreenState

    init {

        viewModelScope.launch{
            preferences.temperatureUnit.collect {
                _settingsScreenState.value = settingsScreenState.value.copy(
                    tempUnit = it
                )
            }

        }
        viewModelScope.launch {

            preferences.windSpeed.collect {
                _settingsScreenState.value = settingsScreenState.value.copy(
                    windSpeed = it
                )
            }
        }
    }

    fun onEvent(
        event: SettingsEvent
    ) {
        when (event) {
            is SettingsEvent.ChangeTemp -> {
                viewModelScope.launch {
                    preferences.changeTempUnit(event.temp)
                }
                _settingsScreenState.value = settingsScreenState.value.copy(
                    tempUnitMenuState = false
                )
            }
            is SettingsEvent.ChangeWindSpeed -> {
                viewModelScope.launch {
                    preferences.changeWindSpeed(event.windSpeed)
                }

                _settingsScreenState.value = settingsScreenState.value.copy(
                    windSpeedMenuState = false
                )
            }
            is SettingsEvent.DismissTempUnitMenu ->{
                _settingsScreenState.value = settingsScreenState.value.copy(
                    tempUnitMenuState = !_settingsScreenState.value.tempUnitMenuState
                )
            }
            is SettingsEvent.SetTempUnitMenuToFalse -> {
                _settingsScreenState.value = settingsScreenState.value.copy(
                    tempUnitMenuState = false
                )
            }
            is SettingsEvent.DismissWindSpeedMenu -> {
                _settingsScreenState.value = settingsScreenState.value.copy(
                    windSpeedMenuState = !_settingsScreenState.value.windSpeedMenuState
                )

            }
            is SettingsEvent.SetWindSpeedMenuToFalse -> {
                _settingsScreenState.value = settingsScreenState.value.copy(
                    windSpeedMenuState = false
                )
            }
        }
    }


}