package com.example.berlingo.repositories

import com.example.berlingo.data.network.Resource
import com.example.berlingo.data.network.responses.JourneysResponse
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.data.network.responses.Trip
import com.example.berlingo.data.network.responses.TripResponse
import com.example.berlingo.data.network.responses.TripsResponse

interface AppRepository {
    suspend fun getLocations(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>>
    suspend fun getJourneys(from: String, toId: String, toLatitude: Double, toLongitude: Double): Resource<JourneysResponse>
    suspend fun getTrips(from: String, to: String, results: Int): Resource<TripsResponse>
    suspend fun getTripById(tripId: String): Resource<TripResponse>
    suspend fun getStopsByDeparture(stopId: String, destination: String, duration: Int): Resource<Stop>

}
