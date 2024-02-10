package com.example.berlingo.di

import com.example.berlingo.journeys.legs.stops.network.StopsApi
import com.example.berlingo.journeys.network.JourneysApi
import com.example.berlingo.map.network.MapsApi
import com.example.berlingo.trips.network.TripsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

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
    fun provideMapsNetwork(): MapsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/maps/api/directions/")
            .client(okHttpClient)
            .build()
            .create(MapsApi::class.java)
    }
}
