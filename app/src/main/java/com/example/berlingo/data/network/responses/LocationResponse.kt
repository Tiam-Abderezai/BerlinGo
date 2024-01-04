package com.example.berlingo.data.network.responses

data class Stop(
    val type: String,
    val id: String,
    val name: String,
    val location: Location
)

data class Location(
    val type: String,
    val id: Int,
    val latitude: String,
    val longitude: String
)