package com.example.weathertoday.data.repository

import android.util.Log
import com.example.weathertoday.data.data_source.remote.WeatherApi
import com.example.weathertoday.data.mappers.toWeatherInfo
import com.example.weathertoday.domain.model.WeatherInfo
import com.example.weathertoday.domain.repository.WeatherRepository
import com.example.weathertoday.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeatherData(
        lat: Double,
        long: Double,
        windSpeed: String?,
        tempUnit: String?
    ): Resource<WeatherInfo> {
        return try {


            Resource.Success(
                data = api.getWeatherData(
                    lat = lat, long = long, tempUnit = tempUnit, windSpeedUnit = windSpeed
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}