package com.example.berlingo.data.network

import com.example.berlingo.data.network.responses.JourneysResponse
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.data.network.responses.Trip
import com.example.berlingo.data.network.responses.TripResponse
import com.example.berlingo.data.network.responses.TripsResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {
    @GET("locations")
    suspend fun getStops(
        @Query("poi") poi: Boolean = false,
        @Query("addresses") addresses: Boolean = false,
        @Query("query") query: String,
    ): Response<List<Stop>>

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
    @GET("journeys")
    suspend fun getJourneys(
        @Query("from") from: String,
        @Query("to") toId: String,
        @Query("to.latitude") toLatitude: Double,
        @Query("to.longitude") toLongitude: Double,
    ): Response<JourneysResponse>

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

    //    @GET("stops/900360136?linesOfStops=true&language=en")
//    suspend fun getStops(
//        @Query("") searchQuery: String,
//    ): Response<List<Stop>>
}
