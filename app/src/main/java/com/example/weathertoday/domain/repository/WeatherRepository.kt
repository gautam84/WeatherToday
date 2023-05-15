package com.example.weathertoday.domain.repository

import com.example.weathertoday.domain.model.WeatherInfo
import com.example.weathertoday.domain.util.Resource

interface WeatherRepository {
    suspend fun getWeatherData(
        lat: Double,
        long: Double,
        windSpeed: String?,
        tempUnit: String?
    ): Resource<WeatherInfo>

}