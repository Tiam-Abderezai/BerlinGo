package com.example.berlingo.journeys.network

import com.example.berlingo.data.network.Resource
import com.example.berlingo.journeys.network.responses.JourneysResponse

interface JourneysRepository {
    suspend fun getJourneys(from: String, toId: String, toLatitude: Double, toLongitude: Double): Resource<JourneysResponse>
}
