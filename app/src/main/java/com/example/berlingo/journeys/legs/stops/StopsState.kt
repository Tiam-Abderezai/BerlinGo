package com.example.berlingo.journeys.legs.stops

import com.example.berlingo.journeys.legs.stops.network.responses.Stop

sealed class StopsState {
    data class Success(val data: List<Stop>) : StopsState()
    data class Error(val message: String) : StopsState()
    object Initial : StopsState()
    object Loading : StopsState()
}