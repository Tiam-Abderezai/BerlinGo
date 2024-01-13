package com.example.berlingo.legs

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.journeys.JourneysApiImpl
import com.example.berlingo.data.network.journeys.responses.Journey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(LegsViewModel::class)

@HiltViewModel
class LegsViewModel @Inject constructor(
    private val journeysApiImpl: JourneysApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow(LegsState(isInitial = true))
    val state: StateFlow<LegsState> = _state.asStateFlow()

    suspend fun handleEvent(event: LegsEvent) {
        when (event) {
            is LegsEvent.LegsGetEvent -> {
                getLegs(event.journey)
            }
        }
    }

    private suspend fun getLegs(journey: Journey) {
//        viewModelScope.launch {
        val legs = journey.legs?.associateWith { it.tripId ?: "" } ?: emptyMap()
        logger.debug("legs: $legs")
        _state.value = LegsState(legs = legs)
//        }
    }
}
