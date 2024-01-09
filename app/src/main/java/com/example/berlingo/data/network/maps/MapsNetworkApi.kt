package com.example.berlingo.data.network.maps

import com.example.berlingo.data.network.responses.maps.DirectionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsNetworkApi {
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
