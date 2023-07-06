/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

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