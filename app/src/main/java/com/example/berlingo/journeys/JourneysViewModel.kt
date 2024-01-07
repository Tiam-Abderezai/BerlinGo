package com.example.berlingo.journeys

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.berlingo.data.network.NetworkApiImpl
import com.example.berlingo.data.network.responses.Leg
import com.example.berlingo.data.network.responses.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class JourneysViewModel @Inject constructor(
    private val networkApiImpl: NetworkApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow(JourneysViewState(isInitial = true))
    val state: StateFlow<JourneysViewState> = _state.asStateFlow()

    suspend fun handleEvent(event: JourneysViewEvent) {
        when (event) {
            is JourneysViewEvent.StopsQueryEvent -> {
                queryStops(event.name)
            }
            is JourneysViewEvent.JourneyQueryEvent -> {
                queryJourneys(event.from, event.to, event.toLatitude, event.toLongitude)
            }
            is JourneysViewEvent.TripQueryEvent -> {
                queryTripById(event.tripId)
            }
        }
    }

    private suspend fun queryStops(query: String) {
        val stops = networkApiImpl.getStops(false, addresses = false, query = query).data
        if (query.isEmpty()) {
            emptyList()
        } else {
            stops?.filter { stop ->
                stop.name?.contains(query, ignoreCase = true) ?: false
            } ?: emptyList()
        }
        _state.value = JourneysViewState(stops = stops)
    }

    private suspend fun queryJourneys(
        from: String,
        toId: String,
        toLatitude: Double,
        toLongitude: Double,
    ) {
        val response = networkApiImpl.getJourneys(
            from = from,
            toId = toId,
            toLatitude = toLatitude,
            toLongitude = toLongitude,
        )
        var legs = mapOf<Leg, String>()
        val journeys = response.data?.journeys?.associateWith { journey ->
            legs = journey.legs?.associateWith { it.tripId ?: "" } ?: emptyMap()
            journey.legs ?: emptyList()
        } ?: emptyMap()
        _state.value = JourneysViewState(journeys = journeys, legs = legs)
    }

    suspend fun queryTrips(from: String, to: String, results: Int) {
        val trips = networkApiImpl.getTrips(from = from, to = to, results)
//        _searchedTrips.value = trips.data?.trips ?: emptyList()
    }

    private suspend fun queryTripById(tripId: String) {
        val trip = networkApiImpl.getTripById(tripId = tripId).data?.trip
        Log.d("dev-log", "stopovers stop: $trip")

        _state.value.trip = trip ?: Trip()
    }
}
