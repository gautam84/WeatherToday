package com.example.weathertoday.domain.location

import android.location.Address
import android.location.Location

interface LocationTracker {


    suspend fun getCurrentLocation(): Location?
}
