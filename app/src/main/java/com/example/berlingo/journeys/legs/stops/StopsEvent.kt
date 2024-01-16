package com.example.berlingo.journeys.legs.stops

import com.example.berlingo.journeys.JourneysEvent

sealed class StopsEvent {
    data class StopsQueryEvent(
        val name: String,
    ) : StopsEvent()
}