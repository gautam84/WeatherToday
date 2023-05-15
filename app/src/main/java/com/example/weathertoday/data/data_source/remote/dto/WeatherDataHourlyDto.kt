package com.example.weathertoday.data.data_source.remote.dto

import com.squareup.moshi.Json

data class WeatherDataHourlyDto(
    val time: List<String>,
    @field:Json(name = "temperature_2m")
    val temperatures: List<Double>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "pressure_msl")
    val pressures: List<Double>,
    @field:Json(name = "windspeed_10m")
    val windSpeeds: List<Double>,
    @field:Json(name = "relativehumidity_2m")
    val humidities: List<Double>,
    @field:Json(name = "visibility")
    val visibilities: List<Double>,
)
