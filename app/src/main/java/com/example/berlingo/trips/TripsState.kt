package com.example.berlingo.trips

sealed class TripsState<out T> {
    object Loading : TripsState<Nothing>()
    data class Success<out T>(val data: T) : TripsState<T>()
    data class Error(val exception: Throwable) : TripsState<Nothing>()
}