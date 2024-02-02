package com.example.berlingo.journeys

sealed class JourneysEvent {
    data class JourneyQueryEvent(
        val from: String,
        val to: String,
        val toLatitude: String,
        val toLongitude: String,
    ) : JourneysEvent()
}
