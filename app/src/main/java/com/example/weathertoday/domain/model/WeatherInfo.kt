package com.example.weathertoday.domain.model

data class WeatherInfo (
    val weatherDataPerHour: Map<Int, List<HourlyWeatherData>>,
    val currentHourlyWeatherData: HourlyWeatherData?,
    val weatherDataPerDay: List<DailyWeatherData>,
    val currentDailyWeatherData: DailyWeatherData?,
)