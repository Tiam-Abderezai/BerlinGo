package com.example.berlingo.map

import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import com.example.berlingo.map.network.responses.Route

data class MapsState(
    val data: List<Route>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)