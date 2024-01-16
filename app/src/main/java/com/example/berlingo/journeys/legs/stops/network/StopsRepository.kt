package com.example.berlingo.journeys.legs.stops.network

import com.example.berlingo.data.network.Resource
import com.example.berlingo.journeys.network.responses.JourneysResponse
import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import com.example.berlingo.trips.network.responses.TripsResponse

interface StopsRepository {
    suspend fun getStops(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>>
    suspend fun getStopsByDeparture(stopId: String, destination: String, duration: Int): Resource<Stop>

}