package com.example.weathertoday.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weathertoday.domain.model.DailyWeatherData
import com.example.weathertoday.domain.model.HourlyWeatherData
import com.example.weathertoday.presentation.util.Screen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Home(
    state: HomeState, navHostController: NavHostController
) {

    if (!state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.error == null) {
                Box(modifier = Modifier.fillMaxSize()) {

                    state.weatherInfo?.currentHourlyWeatherData?.weatherType?.bannerRes?.let {
                        painterResource(
                            id = it
                        )
                    }?.let {
                        Image(
                            painter = it,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(48.dp))
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.Black.copy(0.5f))
                                    .padding(horizontal = 16.dp, vertical = 32.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Your Location",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Updated ${getTimeFromLong(System.currentTimeMillis() - state.updateTime)} ago..",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row {
                                        Text(
                                            text = state.weatherInfo?.currentHourlyWeatherData?.temperature.toString(),
                                            style = MaterialTheme.typography.headlineLarge,
                                            color = Color.White
                                        )

                                        Text(
                                            text = if (state.tempUnit == "celsius") "°C" else "°F",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White
                                        )


                                    }
                                    state.weatherInfo?.currentHourlyWeatherData?.weatherType?.weatherDesc?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Color.White
                                        )
                                    }


                                }
                                Row {
                                    IconButton(onClick = {
                                        navHostController.navigate(Screen.Settings.route)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                }


                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topEnd = 24.dp, topStart = 24.dp
                                    )
                                )
                                .fillMaxSize()
                                .background(
                                    Color.White.copy(0.5f)
                                )
                                .padding(16.dp)

                        ) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))

                                HourlyForecastSection(
                                    state.weatherInfo?.weatherDataPerHour?.get(0),
                                    state.tempUnit
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                DailyForecastSection(
                                    state.weatherInfo?.weatherDataPerDay
                                )
                                Spacer(modifier = Modifier.height(16.dp))


                                state.weatherInfo?.currentDailyWeatherData?.let {
                                    DetailsSection(
                                        it,
                                        state.weatherInfo.currentHourlyWeatherData?.humidity,
                                        state.weatherInfo.currentHourlyWeatherData?.windSpeed,
                                        state.weatherInfo.currentHourlyWeatherData?.visibility,
                                        state.weatherInfo.currentHourlyWeatherData?.pressure,
                                        state.windSpeed
                                    )

                                }


                                Spacer(modifier = Modifier.height(32.dp))
                            }

                        }


                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Some error occurred.", color = Color.Black.copy(0.5f))
                    Text(
                        text = "Please check if GPS is enabled. Also, check if the permissions are granted.",
                        color = Color.Black.copy(0.5f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Error Code: ${state.error}", color = Color.Black.copy(0.5f)
                    )
                }
            }

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

fun getTimeFromLong(milliseconds: Long): String {

    val minutes = milliseconds / 60000

    return when {
        milliseconds < 60000 -> "a little while"
        else -> "$minutes minute(s)"
    }
}

@Composable
fun DetailsSection(
    data: DailyWeatherData,
    humidity: Double?,
    windSpeed: Double?,
    visibility: Double?,
    pressure: Double?,
    unit: String

) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(
            text = "DETAILS",
            color = Color.Black.copy(0.5f),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Column() {
                    Text(text = "Precipitation", color = Color.Black.copy(0.5f))
                    Text(text = "${data.precipitationProbability}%")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Column() {
                    Text(text = "HUM", color = Color.Black.copy(0.5f))
                    Text(text = "${humidity}%")
                    Spacer(modifier = Modifier.height(8.dp))

                }
                Column() {
                    Text(text = "UV Index", color = Color.Black.copy(0.5f))
                    Text(text = data.uvIndex.toString())
                }

            }

            Column() {
                Column() {
                    Text(text = "Wind Speed", color = Color.Black.copy(0.5f))
                    Text(text = "$windSpeed $unit")
                    Spacer(modifier = Modifier.height(8.dp))

                }
                Column() {
                    Text(text = "Visibility", color = Color.Black.copy(0.5f))
                    Text(text = "$visibility mi")
                    Spacer(modifier = Modifier.height(8.dp))

                }
                Column() {
                    Text(text = "Pressure", color = Color.Black.copy(0.5f))
                    Text(text = "$pressure inHg")
                }

            }

        }
    }

}

@Composable
fun HourlyForecastSection(
    list: List<HourlyWeatherData>?,
    unit: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "HOURLY FORECAST",
            color = Color.Black.copy(0.5f),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            if (list != null) {
                items(list.size) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.size(64.dp),
                            painter = painterResource(id = list[it].weatherType.iconRes),
                            contentDescription = null
                        )
                        Text(
                            text = list[it].time.format(DateTimeFormatter.ofPattern("hh:mm a")),
                            color = Color.Black.copy(0.5f)
                        )
                        Text(
                            text = if (unit == "celsius") "${list[it].temperature}°C" else "${list[it].temperature}°F",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }


        }
    }
}

@Composable
fun DailyForecastSection(
    list: List<DailyWeatherData>?
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "DAILY FORECAST",
            color = Color.Black.copy(0.5f),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            if (list != null) {
                items(list.size) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = if (LocalDateTime.now().dayOfMonth == list[it].time.dayOfMonth) {
                                "Today"
                            } else {
                                list[it].time.format(DateTimeFormatter.ofPattern("EEE"))
                            }, style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = list[it].time.format(DateTimeFormatter.ofPattern("dd MMM")),
                            color = Color.Black.copy(0.5f)
                        )

                        Image(
                            modifier = Modifier.size(64.dp),
                            painter = painterResource(id = list[it].weatherType.iconRes),
                            contentDescription = null
                        )
                        Text(
                            text = list[it].weatherType.weatherDesc,
                            color = Color.Black.copy(0.5f),
                            modifier = Modifier.width(80.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
}


