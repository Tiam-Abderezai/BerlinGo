package com.example.berlingo.journeys.legs.stops

import android.location.Location
import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.legs.stops.network.StopsApiImpl
import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(StopsViewModel::class)

@HiltViewModel
class StopsViewModel @Inject constructor(
    private val stopsApiImpl: StopsApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow<StopsState>(StopsState.Initial)
    val state: StateFlow<StopsState> = _state.asStateFlow()

    suspend fun handleEvent(event: StopsEvent) {
        when (event) {
            is StopsEvent.GetStops -> {
                getStops(event.name)
            }

            is StopsEvent.GetNearestStops -> {
                getNearestStop(event.location)
            }

            is StopsEvent.EmptyNearestStop -> {
                _state.value = StopsState.Success(nearestStop = Stop())
            }
            is StopsEvent.EmptyStops -> {
                _state.value = StopsState.Success(stops = emptyList())
            }
        }
    }

    private suspend fun getStops(query: String) {
        try {
            _state.value = StopsState.Loading
            val stops = stopsApiImpl.getStops(false, addresses = false, query = query).data
            if (query.isNotEmpty()) {
                stops?.filter { stop ->
                    stop.name?.contains(query, ignoreCase = true) ?: false
                }
            }
            _state.value = StopsState.Success(stops = stops ?: emptyList())
        } catch (e: Exception) {
            _state.value = StopsState.Error(e.message ?: "Unknown Error")
        }
    }

    private suspend fun getNearestStop(location: Location) {
        val latitude = location.latitude.toString()
        val longitude = location.longitude.toString()
        try {
            _state.value = StopsState.Loading
            val nearestStop = stopsApiImpl.getNearestStops(
                latitude = latitude,
                longitude = longitude,
            ).data?.get(0) ?: Stop()
            _state.value = StopsState.Success(nearestStop = nearestStop)
        } catch (e: Exception) {
            _state.value = StopsState.Error(e.message ?: "Unknown Error")
        }
    }

}
