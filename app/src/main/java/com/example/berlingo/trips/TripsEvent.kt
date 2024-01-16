package com.example.berlingo.trips

import com.example.berlingo.journeys.network.responses.Leg

sealed class TripsEvent {
    data class TripQueryEvent(
        val tripId: String,
        val leg: Leg? = Leg(),
    ) : TripsEvent()
}
