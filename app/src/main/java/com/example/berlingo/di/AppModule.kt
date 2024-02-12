package com.example.berlingo.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.berlingo.journeys.legs.stops.network.StopsApi
import com.example.berlingo.journeys.network.JourneysApi
import com.example.berlingo.map.network.MapsApi
import com.example.berlingo.trips.network.TripsApi
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

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            "encrypted_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }
}
