package com.example.berlingo.journeys

import com.example.berlingo.journeys.network.responses.Journey
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.journeys.network.responses.Remark

sealed class JourneysState {
    object Initial : JourneysState()
    object Loading : JourneysState()
    data class Success(
        val journeys: Map<Journey, List<Leg>>,
        val warningRemark: Remark? = null,
    ) : JourneysState()
    data class Error(val message: String) : JourneysState()

//    data class Warning(val remarks: List<Remark>)
}
