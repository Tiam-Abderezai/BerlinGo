package com.example.berlingo.map

sealed class MapsEvent {
    data class DirectionsGet(
        val origin: String,
        val destination: String,
        val mode: String,
        val transitMode: String,
        val language: String,
    ) : MapsEvent()
}
