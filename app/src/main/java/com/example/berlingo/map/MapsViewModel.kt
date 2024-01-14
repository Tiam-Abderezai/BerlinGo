package com.example.berlingo.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlingo.common.API_KEY_GOOGLE_MAPS
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.journeys.responses.Journey
import com.example.berlingo.data.network.maps.MapsApiImpl
import com.example.berlingo.data.network.maps.responses.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MapsViewModel::class)

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapsApiImpl: MapsApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow(MapsState())
    val state = _state.asStateFlow()

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
        val directions = mapsApiImpl.getDirection(
            key = API_KEY_GOOGLE_MAPS,
            origin = origin,
            destination = destination,
            mode = mode,
            transitMode = transitMode,
            language = language,
        )
        logger.debug("getDirection message:${directions.message}")
        logger.debug("getDirection data:${directions.data} ")

        _state.value = MapsState(routes = directions.data?.routes)
    }

    private suspend fun getJourneyDirections(
        journey: Journey?,
    ) {
        var directions: List<Route>? = emptyList()
        journey?.legs?.forEach { leg ->
            directions = mapsApiImpl.getDirection(
                key = API_KEY_GOOGLE_MAPS,
                origin = leg.origin?.name ?: "",
                destination = leg.destination?.name ?: "",
                mode = "transit",
                transitMode = leg.line?.mode ?: "", // TODO Remove hardcoded value
                language = "en", // TODO Remove hardcoded value
            ).apply {
                logger.debug("getJourneyDirections message:${this.message}")
                logger.debug("getJourneyDirections data:${this.data} ")
            }.data?.routes ?: emptyList()
        }

        _state.value = MapsState(routes = directions)
    }
}
