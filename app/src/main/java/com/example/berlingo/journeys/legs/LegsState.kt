package com.example.berlingo.journeys.legs

import com.example.berlingo.journeys.network.responses.Leg

class LegsState(
    val isInitial: Boolean? = false,
    val isLoading: Boolean? = false,
    val isError: Boolean? = false,
    val isSuccess: Boolean? = false,
    var legs: Map<Leg, String>? = emptyMap(),
)