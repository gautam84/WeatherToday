package com.example.weathertoday.data.mappers

import com.example.weathertoday.data.data_source.remote.dto.WeatherDataDailyDto
import com.example.weathertoday.data.data_source.remote.dto.WeatherDataHourlyDto
import com.example.weathertoday.data.data_source.remote.dto.WeatherDto
import com.example.weathertoday.domain.model.DailyWeatherData
import com.example.weathertoday.domain.model.HourlyWeatherData
import com.example.weathertoday.domain.model.WeatherInfo
import com.example.weathertoday.domain.util.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private data class IndexedHourlyWeatherData(
    val index: Int,
    val data: HourlyWeatherData
)

private data class IndexedDailyWeatherData(
    val index: Int,
    val data: DailyWeatherData
)

fun WeatherDataHourlyDto.toHourlyWeatherDataMap(): Map<Int, List<HourlyWeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        val visibility = visibilities[index]
        IndexedHourlyWeatherData(
            index = index,
            data = HourlyWeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperature = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode),
                visibility = visibility
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

fun WeatherDataDailyDto.toDailyWeatherDataMap(): List<DailyWeatherData> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val uvIndex = uvIndexes[index]
        val precipitationProbability = precipitationProbabilities[index]

        DailyWeatherData(
            time = LocalDateTime.parse(time+ "T00:00", DateTimeFormatter.ISO_DATE_TIME),
            temperature = temperature,
            uvIndex = uvIndex,
            precipitationProbability = precipitationProbability,
            weatherType = WeatherType.fromWMO(weatherCode)

        )

    }
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataPerHour = weatherDataHourly.toHourlyWeatherDataMap()
    val weatherDataPerDay = weatherDataDaily.toDailyWeatherDataMap()

    val now = LocalDateTime.now()
    val currentWeatherDataPerHour = weatherDataPerHour[0]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }

    val dayOfMonth = LocalDateTime.now().dayOfMonth
    val currentWeatherDataPerDay = weatherDataPerDay.find { it.time.dayOfMonth == dayOfMonth }



    return WeatherInfo(
        weatherDataPerHour = weatherDataPerHour,
        currentHourlyWeatherData = currentWeatherDataPerHour,
        weatherDataPerDay = weatherDataPerDay,
        currentDailyWeatherData = currentWeatherDataPerDay
    )

}