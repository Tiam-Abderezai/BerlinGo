package com.example.berlingo.journeys

import com.example.berlingo.journeys.network.responses.Journey
import com.example.berlingo.journeys.network.responses.Leg

sealed class JourneysState {
    data class Success(val data: Map<Journey, List<Leg>>) : JourneysState()
    data class Error(val message: String) : JourneysState()
    object Initial : JourneysState()
    object Loading : JourneysState()
}