package com.example.weathertoday.domain.model

import com.example.weathertoday.domain.util.WeatherType
import java.time.LocalDateTime

data class HourlyWeatherData(
    val time: LocalDateTime,
    val temperature: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val visibility:Double,
    val weatherType: WeatherType
)
