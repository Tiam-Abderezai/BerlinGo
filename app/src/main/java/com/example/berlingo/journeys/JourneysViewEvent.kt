package com.example.berlingo.journeys

import com.example.berlingo.data.network.responses.Stop

sealed class JourneysViewEvent {
    data class JourneyQueryEvent(
        val from: String,
        val to: String,
        val toLatitude: Double,
        val toLongitude: Double,
    ) : JourneysViewEvent()

    data class TripQueryEvent(
        val tripId: String,
    ) : JourneysViewEvent()

    data class StopsQueryEvent(
        val name: String,
    ) : JourneysViewEvent()

}
