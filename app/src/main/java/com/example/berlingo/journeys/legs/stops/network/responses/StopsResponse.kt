package com.example.berlingo.journeys.legs.stops.network.responses

data class Stop(
    val type: String? = null,
    val id: String? = null,
    val name: String? = null,
    val location: Location? = null
)

data class Location(
    val type: String? = null,
    val id: Int? = null,
    val latitude: String? = null,
    val longitude: String? = null,
)