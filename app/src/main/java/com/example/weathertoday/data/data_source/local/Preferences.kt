package com.example.weathertoday.data.data_source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

abstract class PrefsDataStore(context: Context, fileName: String) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(fileName)
    val mDataStore: DataStore<Preferences> = context.dataStore
}


class SettingsImpl(context: Context) : PrefsDataStore(context, SETTINGS_PREFS), Settings {

    companion object {
        private const val SETTINGS_PREFS = "user_login_state"

        private val TEMP_UNIT = stringPreferencesKey("temperature_unit")
        private val WIND_SPEED = stringPreferencesKey("wind_speed")
    }

    override val temperatureUnit: Flow<String>
        get() = mDataStore.data.map { preferences ->
            val tempUnit = preferences[TEMP_UNIT] ?: "celsius"
            tempUnit
        }
    override val windSpeed: Flow<String>
        get() = mDataStore.data.map { preferences ->
            val windSpeed = preferences[WIND_SPEED] ?: "kmh"
            windSpeed
        }

    override suspend fun changeTempUnit(tempUnit: String) {
        mDataStore.edit { preferences ->
            preferences[TEMP_UNIT] = tempUnit
        }
    }

    override suspend fun changeWindSpeed(speedUnit: String) {
        mDataStore.edit { preferences ->
            preferences[WIND_SPEED] = speedUnit
        }
    }


}

@Singleton
interface Settings {
    val temperatureUnit: Flow<String>
    val windSpeed: Flow<String>

    suspend fun changeTempUnit(tempUnit: String)
    suspend fun changeWindSpeed(speedUnit: String)

}