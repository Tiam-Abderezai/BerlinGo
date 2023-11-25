package com.example.berlingo.data.network

import com.example.berlingo.data.network.responses.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("locations?poi=false&addresses=false&query=mehringdamm/")
    suspend fun getLocations(
        @Query("") searchQuery: String,
    ): Response<List<LocationResponse>>
    @GET("stops/900360136?linesOfStops=true&language=en")
    suspend fun getStops(
        @Query("") searchQuery: String,
    ): Response<List<LocationResponse>>
    @GET("trips/")
    suspend fun getTrips(
        @Query("") searchQuery: String,
    ): Response<List<LocationResponse>>
//    @GET("locations?poi=false&addresses=false&query=mehringdamm/") TODO
//    suspend fun getJourneys(
//        @Query("") searchQuery: String,
//    ): Response<List<LocationResponse>>
//    @GET("locations?poi=false&addresses=false&query=mehringdamm/") TODO
//    suspend fun getLocationsNearby(
//        @Query("") searchQuery: String,
//    ): Response<List<LocationResponse>>
//    @GET("locations?poi=false&addresses=false&query=mehringdamm/") TODO
//    suspend fun getRadar(
//        @Query("") searchQuery: String,
//    ): Response<List<LocationResponse>>
//    @GET("locations?poi=false&addresses=false&query=mehringdamm/") TODO
//    suspend fun getReachableFrom(
//        @Query("") searchQuery: String,
//    ): Response<List<LocationResponse>>
    @GET("stops/900360136/departures?duration=10&results=4&linesOfStops=true&remarks=true&language=en")
    suspend fun getDepartures(
        @Query("") searchQuery: String,
    ): Response<List<LocationResponse>>
    @GET("stops/900360136/arrivals?duration=10&linesOfStops=false&remarks=true&language=en")
    suspend fun getArrivals(
        @Query("") searchQuery: String,
    ): Response<List<LocationResponse>>
}