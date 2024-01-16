package com.example.berlingo.trips

import com.example.berlingo.journeys.JourneysEvent

sealed class TripsEvent {
    data class TripQueryEvent(
        val tripId: String,
    ) : TripsEvent()

}