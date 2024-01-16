package com.example.berlingo.map.network

import com.example.berlingo.map.network.responses.DirectionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {
    @GET("json")
    suspend fun getDirection(
        @Query("key") key: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("transit_mode") transitMode: String,
        @Query("language") language: String,
    ): Response<DirectionsResponse>
}
