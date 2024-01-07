package com.example.berlingo.journeys

import com.example.berlingo.data.network.responses.Journey
import com.example.berlingo.data.network.responses.Leg
import com.example.berlingo.data.network.responses.Location
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.data.network.responses.Trip

data class JourneysViewState(
    val isInitial: Boolean? = false,
    val isLoading: Boolean? = false,
    val isError: Boolean? = false,
    val isSuccess: Boolean? = false,
    val originStop: Stop? = null,
    val destinationStop: Stop? = null,
    val originStopFocused: Boolean? = false,
    val destinationStopFocused: Boolean? = false,
    val stops: List<Stop>? = emptyList(),
    val location: Location? = null,
    var journeys: Map<Journey, List<Leg>>? = emptyMap(),
    var legs: Map<Leg, String>? = emptyMap(),
    var trip: Trip? = Trip(),
)
