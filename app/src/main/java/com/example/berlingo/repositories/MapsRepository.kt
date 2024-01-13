package com.example.berlingo.repositories

import com.example.berlingo.data.network.Resource
import com.example.berlingo.data.network.maps.responses.DirectionsResponse

interface MapsRepository {
    suspend fun getDirection(key: String, origin: String, destination: String, mode: String, transitMode: String, language: String): Resource<DirectionsResponse>
}