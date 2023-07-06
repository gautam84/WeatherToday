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