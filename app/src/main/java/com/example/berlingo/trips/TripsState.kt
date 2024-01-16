package com.example.berlingo.trips

import com.example.berlingo.trips.network.responses.Trip

sealed class TripsState() {
    object Initial : TripsState()
    object Loading : TripsState()
    data class Success(
        val tripsData: List<Trip> = emptyList(),
        val stopoversData: List<Trip.Stopover>? = null,
        val tripData: Trip? = null,
        val originStopId: String? = "",
        val destinStopId: String? = "",
    ) : TripsState()
    data class Error(val message: String) : TripsState()
}
