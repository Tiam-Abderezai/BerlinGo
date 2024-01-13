package com.example.berlingo.journeys

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.journeys.JourneysApiImpl
import com.example.berlingo.data.network.journeys.responses.Leg
import com.example.berlingo.data.network.journeys.responses.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(JourneysViewModel::class)
@HiltViewModel
class JourneysViewModel @Inject constructor(
    private val journeysApiImpl: JourneysApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow(JourneysState(isInitial = true))
    val state: StateFlow<JourneysState> = _state.asStateFlow()

    suspend fun handleEvent(event: JourneysEvent) {
        when (event) {
            is JourneysEvent.StopsQueryEvent -> {
                queryStops(event.name)
            }
//            is JourneysEvent.LegsGetEvent -> {
//                getLegs(event.journey)
//            }
            is JourneysEvent.JourneyQueryEvent -> {
                queryJourneys(event.from, event.to, event.toLatitude, event.toLongitude)
            }
            is JourneysEvent.TripQueryEvent -> {
                queryTripById(event.tripId)
            }
        }
    }

    private suspend fun queryStops(query: String) {
        val stops = journeysApiImpl.getStops(false, addresses = false, query = query).data
        if (query.isEmpty()) {
            emptyList()
        } else {
            stops?.filter { stop ->
                stop.name?.contains(query, ignoreCase = true) ?: false
            } ?: emptyList()
        }
        _state.value = JourneysState(stops = stops)
    }

    private suspend fun queryJourneys(
        from: String,
        toId: String,
        toLatitude: Double,
        toLongitude: Double,
    ) {
        val response = journeysApiImpl.getJourneys(
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
        logger.debug("response: ${response.data?.journeys}")
        logger.debug("journeys: $journeys")
        logger.debug("legs: $legs")

        _state.value = JourneysState(journeys = journeys)
    }

    suspend fun queryTrips(from: String, to: String, results: Int) {
        val trips = journeysApiImpl.getTrips(from = from, to = to, results)
//        _searchedTrips.value = trips.data?.trips ?: emptyList()
    }

    private suspend fun queryTripById(tripId: String) {
        val trip = journeysApiImpl.getTripById(tripId = tripId).data?.trip
        logger.debug("stopovers stop: $trip")

        _state.value.trip = trip ?: Trip()
    }
}
