package com.example.weathertoday.data.data_source.remote.dto

import com.squareup.moshi.Json

data class WeatherDto(
    @field:Json(name = "hourly")
    val weatherDataHourly: WeatherDataHourlyDto,
    @field:Json(name = "daily")
    val weatherDataDaily: WeatherDataDailyDto
)