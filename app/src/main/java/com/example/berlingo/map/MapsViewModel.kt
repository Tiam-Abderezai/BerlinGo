package com.example.berlingo.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlingo.common.API_KEY_GOOGLE_MAPS
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.network.responses.Journey
import com.example.berlingo.map.network.MapsApiImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MapsViewModel::class)

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapsApiImpl: MapsApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow<MapsState>(MapsState.Initial)
    val state: StateFlow<MapsState> = _state.asStateFlow()

    fun handleEvent(event: MapsEvent) {
        when (event) {
            is MapsEvent.DirectionsGet -> {
                viewModelScope.launch {
                    getDirection(
                        event.origin,
                        event.destination,
                        event.mode,
                        event.transitMode,
                        event.language,
                    )
                }
            }

            is MapsEvent.DirectionsJourneyGet -> {
                viewModelScope.launch {
                    getJourneyDirections(event.journey)
                }
            }
        }
    }

    private suspend fun getDirection(
        origin: String,
        destination: String,
        mode: String,
        transitMode: String,
        language: String,
    ) {
        try {
            _state.value = MapsState.Loading
            val directions = mapsApiImpl.getDirection(
                key = API_KEY_GOOGLE_MAPS,
                origin = origin,
                destination = destination,
                mode = mode,
                transitMode = transitMode,
                language = language,
            ).data?.routes ?: emptyList()
            _state.value = MapsState.Success(data = directions)
        } catch (e: Exception) {
            _state.value = MapsState.Error(e.message ?: "Unknown Error")
        }
    }

    private suspend fun getJourneyDirections(
        journey: Journey?,
    ) {
        try {
            _state.value = MapsState.Loading
            journey?.legs?.forEach { leg ->
                val directions = mapsApiImpl.getDirection(
                    key = API_KEY_GOOGLE_MAPS,
                    origin = leg.origin?.name ?: "",
                    destination = leg.destination?.name ?: "",
                    mode = "transit",
                    transitMode = leg.line?.mode ?: "", // TODO Remove hardcoded value
                    language = "en", // TODO Remove hardcoded value
                ).data?.routes ?: emptyList()
                _state.value = MapsState.Success(data = directions)
            }
        } catch (e: Exception) {
            _state.value = MapsState.Error(message = e.message ?: "Unknown Error")
        }
    }
}
