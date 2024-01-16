package com.example.berlingo.journeys.legs

import com.example.berlingo.journeys.network.responses.Journey

sealed class LegsEvent {

    data class LegsGetEvent(
        val journey: Journey,
    ) : LegsEvent()
}
