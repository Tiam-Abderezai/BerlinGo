package com.example.berlingo.legs

import com.example.berlingo.data.network.journeys.responses.Journey

sealed class LegsEvent {

    data class LegsGetEvent(
        val journey: Journey,
    ) : LegsEvent()
}
