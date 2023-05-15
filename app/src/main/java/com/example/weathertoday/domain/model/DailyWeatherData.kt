package com.example.weathertoday.domain.model

import com.example.weathertoday.domain.util.WeatherType
import java.time.LocalDateTime


data class DailyWeatherData(
    val time: LocalDateTime,
    val temperature: Double,
    val uvIndex: Double,
    val precipitationProbability: Int,
    val weatherType: WeatherType
)
