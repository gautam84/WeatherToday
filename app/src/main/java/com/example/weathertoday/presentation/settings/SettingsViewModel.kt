/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

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