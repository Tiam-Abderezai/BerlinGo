package com.example.berlingo.journeys.legs.stops

import android.location.Location
import com.example.berlingo.journeys.legs.stops.network.responses.Stop

sealed class StopsEvent {
    data class GetStops(
        val name: String,
    ) : StopsEvent()
    data class GetNearestStops(
        val location: Location,
    ) : StopsEvent()
    data class EmptyNearestStop(val emptyStop: Stop = Stop()) : StopsEvent()
    data class EmptyStops(val emptyStops: List<Stop> = emptyList()) : StopsEvent()
}