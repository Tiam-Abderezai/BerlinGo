package com.example.berlingo.repositories

import com.example.berlingo.data.network.Resource
import com.example.berlingo.data.network.journeys.responses.JourneysResponse
import com.example.berlingo.data.network.journeys.responses.Stop
import com.example.berlingo.data.network.journeys.responses.TripResponse
import com.example.berlingo.data.network.journeys.responses.TripsResponse

interface JourneysRepository {
    suspend fun getStops(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>>
    suspend fun getJourneys(from: String, toId: String, toLatitude: Double, toLongitude: Double): Resource<JourneysResponse>
    suspend fun getTrips(from: String, to: String, results: Int): Resource<TripsResponse>
    suspend fun getTripById(tripId: String): Resource<TripResponse>
    suspend fun getStopsByDeparture(stopId: String, destination: String, duration: Int): Resource<Stop>

}
