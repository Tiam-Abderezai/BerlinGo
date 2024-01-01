package com.example.berlingo.repositories

import com.example.berlingo.data.network.Resource
import com.example.berlingo.data.network.responses.JourneysResponse
import com.example.berlingo.data.network.responses.Stop
import com.google.gson.JsonObject

interface AppRepository {
    suspend fun getLocations(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>>
    suspend fun getJourneys(from: String, toId: String, toName: String, toLatitude: Double, toLongitude: Double): Resource<JourneysResponse>

}