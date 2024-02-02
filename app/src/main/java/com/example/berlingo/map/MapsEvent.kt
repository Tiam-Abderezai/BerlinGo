package com.example.berlingo.map

import com.example.berlingo.journeys.network.responses.Journey

sealed class MapsEvent {
    data class DirectionsJourneyGet(
        val journey: Journey,
    ) : MapsEvent()
}
