package com.example.weathertoday.data.data_source.remote.dto

import com.squareup.moshi.Json

data class WeatherDataDailyDto(
    val time: List<String>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "temperature_2m_max")
    val temperatures: List<Double>,
    @field:Json(name = "uv_index_max")
    val uvIndexes: List<Double>,
    @field:Json(name = "precipitation_probability_max")
    val precipitationProbabilities: List<Int>,

    )