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
}