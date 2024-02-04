package com.example.berlingo.journeys.legs.stops.network

import com.example.berlingo.data.network.Resource
import com.example.berlingo.journeys.legs.stops.network.responses.Stop

interface StopsRepository {
    suspend fun getStops(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>>
    suspend fun getNearestStops(latitude: String, longitude: String): Resource<List<Stop>>
    suspend fun getStopsByDeparture(stopId: String, destination: String, duration: Int): Resource<Stop>

}