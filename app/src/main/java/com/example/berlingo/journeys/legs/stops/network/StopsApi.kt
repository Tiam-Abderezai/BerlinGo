package com.example.berlingo.journeys.legs.stops.network

import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StopsApi {
    @GET("locations")
    suspend fun getStops(
        @Query("poi") poi: Boolean = false,
        @Query("addresses") addresses: Boolean = false,
        @Query("query") query: String,
    ): Response<List<Stop>>

    @GET("stops/{stopId}/departures")
    suspend fun getStopsByDeparture(
        @Path("stopId") stopId: String,
        @Query("direction") direction: String,
        @Query("duration") duration: Int,
    ): Response<Stop>

    @GET("stops/900360136/departures?duration=10&results=4&linesOfStops=true&remarks=true&language=en")
    suspend fun getDepartures(
        @Query("") searchQuery: String,
    ): Response<List<Stop>>

    @GET("stops/900360136/arrivals?duration=10&linesOfStops=false&remarks=true&language=en")
    suspend fun getArrivals(
        @Query("") searchQuery: String,
    ): Response<List<Stop>>
}
