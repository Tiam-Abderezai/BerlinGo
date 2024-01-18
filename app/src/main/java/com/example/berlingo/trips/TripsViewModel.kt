package com.example.berlingo.trips

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.trips.network.TripsApiImpl
import com.example.berlingo.trips.network.responses.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(TripsViewModel::class)

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val tripsApiImpl: TripsApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow<TripsState>(TripsState.Initial)
    val state: StateFlow<TripsState> = _state.asStateFlow()

    suspend fun handleEvent(event: TripsEvent) {
        when (event) {
            is TripsEvent.TripQueryEvent -> {
                queryTripById(
                    event.tripId,
                    event.leg,
                )
            }
        }
    }

    private suspend fun queryTripById(tripId: String, leg: Leg?) {
        try {
            _state.value = TripsState.Loading
            val stopOvers = tripsApiImpl.getTripById(tripId = tripId).data?.trip?.stopovers
            val stopovers = mutableListOf(Trip.Stopover())
            var addStopovers = false
            stopOvers?.map { stopover ->
                if (stopover.stop?.name != null) {
                    val originMatched = stopover.stop.id == leg?.origin?.id
                    val destinMatched = stopover.stop.id == leg?.destination?.id
                    if (originMatched) {
                        addStopovers = true
                    }
                    if (addStopovers) {
                        stopovers.add(stopover)
                    }
                    if (destinMatched) {
                        addStopovers = false
                    }
                }
            } ?: ""
            _state.value = TripsState.Success(stopoversData = stopovers)
        } catch (e: Exception) {
            _state.value = TripsState.Error(message = e.message ?: "Unknown Error")
        }
    }
}
