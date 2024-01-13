package com.example.berlingo.map

import com.example.berlingo.data.network.maps.responses.Route
import com.google.maps.android.compose.MapProperties

class MapsState(
    val properties: MapProperties? = MapProperties(),
    val routes: List<Route>? = listOf(Route()),
    val isLoading: Boolean? = false,
    val isError: Boolean? = false,
)
