package com.example.weathertoday.di

import android.app.Application
import android.content.Context
import com.example.weathertoday.data.data_source.local.Settings
import com.example.weathertoday.data.data_source.local.SettingsImpl
import com.example.weathertoday.data.data_source.remote.WeatherApi
import com.example.weathertoday.data.location.DefaultLocationTracker
import com.example.weathertoday.data.repository.WeatherRepositoryImpl
import com.example.weathertoday.domain.location.LocationTracker
import com.example.weathertoday.domain.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    fun providesLocationTracker(
        locationClient: FusedLocationProviderClient, app: Application
    ): LocationTracker = DefaultLocationTracker(locationClient = locationClient, application = app)

    @Provides
    @Singleton
    fun providesRepository(api: WeatherApi): WeatherRepository = WeatherRepositoryImpl(api)

    @Provides
    @Singleton
    fun providesPrefs(@ApplicationContext context: Context): Settings = SettingsImpl(context)


}