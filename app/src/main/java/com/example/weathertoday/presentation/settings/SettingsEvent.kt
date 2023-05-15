package com.example.weathertoday.presentation.settings

sealed class SettingsEvent {

    data class ChangeTemp(val temp: String) : SettingsEvent()
    data class ChangeWindSpeed(val windSpeed: String) : SettingsEvent()
    object DismissTempUnitMenu : SettingsEvent()
    object SetTempUnitMenuToFalse : SettingsEvent()

    object DismissWindSpeedMenu : SettingsEvent()
    object SetWindSpeedMenuToFalse : SettingsEvent()

}