package com.example.berlingo.journeys

sealed class JourneysEvent {
    data class JourneyQueryEvent(
        val from: String,
        val to: String,
        val toLatitude: Double,
        val toLongitude: Double,
    ) : JourneysEvent()
    data class TripQueryEvent(
        val tripId: String,
    ) : JourneysEvent()

    data class StopsQueryEvent(
        val name: String,
    ) : JourneysEvent()
}
