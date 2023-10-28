package com.example.berlingo.repositories

import com.example.berlingo.data.network.Resource
import com.example.berlingo.data.network.responses.LocationResponse

interface AppRepository {
    suspend fun getLocations(): Resource<List<LocationResponse>>
}