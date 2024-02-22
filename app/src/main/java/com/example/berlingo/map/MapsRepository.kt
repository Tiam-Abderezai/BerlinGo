package com.example.berlingo.map

import com.example.berlingo.common.Resource
import com.example.berlingo.map.network.responses.DirectionsResponse

interface MapsRepository {
    suspend fun getDirection(key: String, origin: String, destination: String, mode: String, transitMode: String, language: String): Resource<DirectionsResponse>
}