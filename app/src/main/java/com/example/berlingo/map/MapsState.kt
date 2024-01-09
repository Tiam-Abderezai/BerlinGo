package com.example.berlingo.map

import com.example.berlingo.data.network.responses.maps.Route
import com.google.maps.android.compose.MapProperties

class MapsState(
//    val repositories: List<RepositoryDto?>? = emptyList<RepositoryDto>(),
    val properties: MapProperties? = MapProperties(),
    val routes: List<Route>? = listOf(Route()),
    val isLoading: Boolean? = false,
    val isError: Boolean? = false,
)
