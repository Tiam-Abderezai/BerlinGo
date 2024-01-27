package com.example.berlingo.journeys.network

import com.example.berlingo.journeys.network.responses.JourneysResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface JourneysApi {
    @GET("journeys")
    suspend fun getJourneys(
        @Query("from") from: String,
        @Query("to") toId: String,
        @Query("to.latitude") toLatitude: Double,
        @Query("to.longitude") toLongitude: Double,
        @Query("language") language: String = Locale.getDefault().language,
        @Query("results") results: Int = 10,
    ): Response<JourneysResponse>
}
