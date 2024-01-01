package com.example.berlingo.data.network

import com.example.berlingo.data.network.responses.JourneysResponse
import com.example.berlingo.data.network.responses.Stop
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

//    @GET("locations?poi=false&addresses=false&query=/")
//    suspend fun getLocations(
//        @Query("id") query: String,
//    ): Response<List<LocationResponse>>

    @GET("locations")
    suspend fun getLocations(
        @Query("poi") poi: Boolean = false,
        @Query("addresses") addresses: Boolean = false,
        @Query("query") query: String,
    ): Response<List<Stop>>

    @GET("stops/900360136?linesOfStops=true&language=en")
    suspend fun getStops(
        @Query("") searchQuery: String,
    ): Response<List<Stop>>

    @GET("trips/")
    suspend fun getTrips(
        @Query("") searchQuery: String,
    ): Response<List<Stop>>

//    @GET("journeys?from=900023201&to.id=900980720&to.name=ATZE+Musiktheater&to.latitude=52.54333&to.longitude=13.35167")
//    suspend fun getJourneys(
//        @Query("") searchQuery: String,
//    ): Response<JsonObject>

    @GET("journeys")
    suspend fun getJourneys(
        @Query("from") from: String,
        @Query("to.id") toId: String,
        @Query("to.name") toName: String,
        @Query("to.latitude") toLatitude: Double,
        @Query("to.longitude") toLongitude: Double,
    ): Response<JourneysResponse>

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
    ): Response<List<Stop>>

    @GET("stops/900360136/arrivals?duration=10&linesOfStops=false&remarks=true&language=en")
    suspend fun getArrivals(
        @Query("") searchQuery: String,
    ): Response<List<Stop>>
}
