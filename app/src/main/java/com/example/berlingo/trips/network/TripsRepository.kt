package com.example.berlingo.trips.network

import com.example.berlingo.data.network.Resource
import com.example.berlingo.trips.network.responses.TripResponse
import com.example.berlingo.trips.network.responses.TripsResponse

interface TripsRepository {
    suspend fun getTrips(from: String, to: String, results: Int): Resource<TripsResponse>
    suspend fun getTripById(tripId: String): Resource<TripResponse>
}
