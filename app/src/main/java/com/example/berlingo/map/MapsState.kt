package com.example.berlingo.map

import com.example.berlingo.map.network.responses.Route

sealed class MapsState {
    object Initial : MapsState()
    object Loading : MapsState()
    data class Success(val data: List<Route>) : MapsState()
    data class Error(val message: String) : MapsState()
}