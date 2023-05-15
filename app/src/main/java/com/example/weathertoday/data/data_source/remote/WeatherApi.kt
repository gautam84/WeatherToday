package com.example.weathertoday.data.data_source.remote

import com.example.weathertoday.data.data_source.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast?hourly=temperature_2m,relativehumidity_2m,weathercode,pressure_msl,windspeed_10m,visibility&daily=weathercode,temperature_2m_max,uv_index_max,precipitation_probability_max&timezone=auto")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("temperature_unit") tempUnit: String? = "celsius",
        @Query("windspeed_unit") windSpeedUnit: String? = "kmh"
    ): WeatherDto

}