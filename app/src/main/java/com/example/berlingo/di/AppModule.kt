package com.example.berlingo.di

import com.example.berlingo.common.API_KEY_GOOGLE_MAPS
import com.example.berlingo.data.network.NetworkApi
import com.example.berlingo.data.network.maps.MapsNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideNetwork(): NetworkApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://v6.bvg.transport.rest/")
            .client(okHttpClient)
            .build()
            .create(NetworkApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMapsNetwork(): MapsNetworkApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/maps/api/directions/")
            .client(okHttpClient)
            .build()
            .create(MapsNetworkApi::class.java)
    }
}
