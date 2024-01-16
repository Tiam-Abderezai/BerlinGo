package com.example.berlingo.trips.network

import com.example.berlingo.trips.network.responses.TripResponse
import com.example.berlingo.trips.network.responses.TripsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TripsApi {
    @GET("trips")
    suspend fun getTrips(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("results") results: Int,
    ): Response<TripsResponse>

    @GET("trips/{tripId}")
    suspend fun getTripById(
        @Path("tripId") tripId: String,
        @Query("stopovers") stopovers: Boolean = true,
        @Query("results") results: Int = 1,
    ): Response<TripResponse>

}