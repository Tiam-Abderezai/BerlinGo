package com.example.berlingo.trips

import com.example.berlingo.trips.network.responses.Trip

sealed class TripsState() {
    object Initial : TripsState()
    object Loading : TripsState()
    data class Success(
        val tripsData: List<Trip> = emptyList(),
        val tripData: Trip? = null,
    ) : TripsState()
    data class Error(val message: String) : TripsState()
}
