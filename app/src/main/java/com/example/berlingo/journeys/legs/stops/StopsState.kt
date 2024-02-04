package com.example.berlingo.journeys.legs.stops

import com.example.berlingo.journeys.legs.stops.network.responses.Stop

sealed class StopsState {
    object Initial : StopsState()
    object Loading : StopsState()
    data class Success(val stops: List<Stop> = emptyList(), val nearestStop: Stop = Stop()) : StopsState()
    data class Error(val message: String) : StopsState()
}