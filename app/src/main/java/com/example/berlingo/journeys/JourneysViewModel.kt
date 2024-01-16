package com.example.berlingo.journeys

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.network.JourneysApiImpl
import com.example.berlingo.journeys.network.responses.Leg
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
    private val _state = MutableStateFlow<JourneysState>(JourneysState.Initial)
    val state: StateFlow<JourneysState> = _state.asStateFlow()

    suspend fun handleEvent(event: JourneysEvent) {
        when (event) {
            is JourneysEvent.JourneyQueryEvent -> {
                queryJourneys(event.from, event.to, event.toLatitude, event.toLongitude)
            }
        }
    }

    private suspend fun queryJourneys(
        from: String,
        toId: String,
        toLatitude: Double,
        toLongitude: Double,
    ) {
        try {
            _state.value = JourneysState.Loading
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
            _state.value = JourneysState.Success(data = journeys)
        } catch (e: Exception) {
            _state.value = JourneysState.Error(message = e.message ?: "Unknown Error")
        }
    }
}
