package com.example.berlingo.routes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlingo.data.network.NetworkApiImpl
import com.example.berlingo.data.network.responses.Journeys
import com.example.berlingo.data.network.responses.Leg
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.data.network.responses.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val networkApiImpl: NetworkApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow(RoutesViewState())
    val state = _state.asStateFlow()

    private val _stateTextOrigin = MutableStateFlow("")
    val stateTextOrigin: StateFlow<String> = _stateTextOrigin

    private val _stateTextDestination = MutableStateFlow("")
    val stateTextDestination: StateFlow<String> = _stateTextDestination

    private val _searchLocations = MutableStateFlow<List<Stop>>(emptyList())
    val searchLocations: StateFlow<List<Stop>> = _searchLocations

    private val _selectedLocation = MutableStateFlow("")
    val selectedLocation: StateFlow<String> = _selectedLocation

    private val _searchedJourneys = MutableStateFlow<List<Journeys>>(emptyList())
    val searchedJourneys: StateFlow<List<Journeys>> = _searchedJourneys

    private val _searchedLegs = MutableStateFlow<List<Leg>>(emptyList())
    val searchedLegs: StateFlow<List<Leg>> = _searchedLegs

    private val _searchedTrips = MutableStateFlow<List<Trip>>(emptyList())
    val searchedTrips: StateFlow<List<Trip>> = _searchedTrips

    private val _searchedStopovers = MutableStateFlow<List<Trip.Stopover>>(emptyList())
    val searchedStopovers: StateFlow<List<Trip.Stopover>> = _searchedStopovers

    private var legsMutableList: MutableList<Leg> = mutableListOf()

    init {
        viewModelScope.launch {
        }
    }

    suspend fun queryLocation(query: String) {
        val locations = networkApiImpl.getLocations(false, addresses = false, query = query)

        _searchLocations.value = if (query.isEmpty()) {
            emptyList()
        } else {
            locations.data?.filter {
                it.name.contains(query, ignoreCase = true)
            } ?: emptyList()
        }
    }

    suspend fun queryJourneys(from: String, toId: String, toLatitude: Double, toLongitude: Double) {
        val response = networkApiImpl.getJourneys(
            from = from,
            toId = toId,
            toLatitude = toLatitude,
            toLongitude = toLongitude,
        )
        val journeysList = response.data?.journeys
        _searchedJourneys.value = journeysList ?: emptyList()
        journeysList?.forEachIndexed { index, journey ->
            journey.legs?.forEach {
                legsMutableList.add(it)
            }
        }
//        Log.d("dev-log", "legs size: ${legsMutableList.size}")
        _searchedLegs.value = legsMutableList
    }

    suspend fun queryTrips(from: String, to: String, results: Int) {
        val trips = networkApiImpl.getTrips(from = from, to = to, results)
        _searchedTrips.value = trips.data?.trips ?: emptyList()
    }

    suspend fun queryTripById(tripId: String) {
        val trip = networkApiImpl.getTripById(tripId = tripId)
        Log.d("dev-log", "stopovers stop: ${trip.data}")
        _searchedStopovers.value = trip.data?.trip?.stopovers ?: emptyList()
    }
}
