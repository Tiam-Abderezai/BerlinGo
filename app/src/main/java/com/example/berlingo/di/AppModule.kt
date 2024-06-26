package com.example.berlingo.di

import android.content.Context
import com.example.berlingo.journeys.legs.stops.network.StopsApi
import com.example.berlingo.journeys.legs.stops.network.StopsRepository
import com.example.berlingo.journeys.legs.stops.network.StopsRepositoryImpl
import com.example.berlingo.journeys.network.JourneysApi
import com.example.berlingo.journeys.network.JourneysRepository
import com.example.berlingo.journeys.network.JourneysRepositoryImpl
import com.example.berlingo.map.MapsRepository
import com.example.berlingo.map.network.MapsApi
import com.example.berlingo.map.network.MapsRepositoryImpl
import com.example.berlingo.settings.SettingsRepository
import com.example.berlingo.trips.network.TripsApi
import com.example.berlingo.trips.network.TripsRepository
import com.example.berlingo.trips.network.TripsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private const val timeout = 1000.toLong()
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(timeout + 100, TimeUnit.MILLISECONDS)
        .readTimeout(timeout, TimeUnit.MILLISECONDS)
        .writeTimeout(timeout, TimeUnit.MILLISECONDS)
        .build()

    @Singleton
    @Provides
    fun provideJourneysRepository(
        journeysApi: JourneysApi
    ) = JourneysRepositoryImpl(journeysApi) as JourneysRepository

    @Singleton
    @Provides
    fun provideJourneysNetwork(): JourneysApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://v6.bvg.transport.rest/")
            .client(okHttpClient)
            .build()
            .create(JourneysApi::class.java)
    }

    @Singleton
    @Provides
    fun provideStopsRepository(
        stopsApi: StopsApi
    ) = StopsRepositoryImpl(stopsApi) as StopsRepository

    @Singleton
    @Provides
    fun provideStopsNetwork(): StopsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://v6.bvg.transport.rest/")
            .client(okHttpClient)
            .build()
            .create(StopsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTripsRepository(
        tripsApi: TripsApi
    ) = TripsRepositoryImpl(tripsApi) as TripsRepository

    @Singleton
    @Provides
    fun provideTripsApiNetwork(): TripsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://v6.bvg.transport.rest/")
            .client(okHttpClient)
            .build()
            .create(TripsApi::class.java)
    }
    @Singleton
    @Provides
    fun provideMapsRepository(
        mapsApi: MapsApi
    ) = MapsRepositoryImpl(mapsApi) as MapsRepository
    @Singleton
    @Provides
    fun provideMapsNetwork(): MapsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/maps/api/directions/")
            .client(okHttpClient)
            .build()
            .create(MapsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository = SettingsRepository(context)
}
