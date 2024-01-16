package com.example.berlingo.map

import com.example.berlingo.journeys.network.responses.Journey

sealed class MapsEvent {
    data class DirectionsGet(
        val origin: String,
        val destination: String,
        val mode: String,
        val transitMode: String,
        val language: String,
    ) : MapsEvent()

    data class DirectionsJourneyGet(
        val journey: Journey,
    ) : MapsEvent()
}
