package com.example.berlingo.legs

import com.example.berlingo.data.network.journeys.responses.Leg

class LegsState(
    val isInitial: Boolean? = false,
    val isLoading: Boolean? = false,
    val isError: Boolean? = false,
    val isSuccess: Boolean? = false,
    var legs: Map<Leg, String>? = emptyMap(),
)