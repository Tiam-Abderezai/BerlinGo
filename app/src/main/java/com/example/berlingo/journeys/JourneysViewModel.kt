package com.example.berlingo.journeys

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.network.JourneysRepository
import com.example.berlingo.journeys.network.JourneysRepositoryImpl
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.journeys.network.responses.Remark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(JourneysViewModel::class)

@HiltViewModel
class JourneysViewModel @Inject constructor(
    private val journeysRepository: JourneysRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<JourneysState>(JourneysState.Initial)
    val state: StateFlow<JourneysState> = _state.asStateFlow()

    suspend fun handleEvent(event: JourneysEvent) {
        when (event) {
            is JourneysEvent.GetJourneys -> getJourneys(event.from, event.to, event.toLatitude, event.toLongitude)
        }
    }

    private suspend fun getJourneys(
        from: String,
        toId: String,
        toLatitude: String,
        toLongitude: String,
    ) {
        try {
            _state.value = JourneysState.Loading
            val response = journeysRepository.getJourneys(
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

            var warningRemark = Remark()
            val remarks = legs.map { leg ->
                leg.key.remarks?.map { remark ->
                    if (remark.type?.contains("warning") == true) {
                        warningRemark = remark
                    }
                }
            }
            logger.debug("response message: ${response.message}")
            _state.value = JourneysState.Success(journeys = journeys, warningRemark = warningRemark)
        } catch (e: Exception) {
            _state.value = JourneysState.Error(message = e.message ?: "Unknown Error")
        }
    }
}
