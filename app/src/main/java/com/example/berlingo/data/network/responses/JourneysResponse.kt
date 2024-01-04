package com.example.berlingo.data.network.responses

import java.math.BigDecimal

data class JourneysResponse(
    val journeys: List<Journeys>,
)

data class Journeys(
    val type: String? = "",
    val legs: List<Leg>? = emptyList(),
)

data class Leg(
    val origin: Origin? = null,
    val destination: Destination? = null,
    val departure: String? = "",
    val plannedDeparture: String? = "",
    val departureDelay: Int? = 0,
    val arrival: String? = "",
    val reachable: Boolean? = false,
    val tripId: String? = "",
    val line: Line? = null,
    val direction: String? = "",
    val currentLocation: CurrentLocation? = null,
    val arrivalPlatform: String? = "",
    val plannedArrivalPlatform: String? = "",
    val arrivalPrognosisType: String? = "",
    val departurePlatform: String? = "",
    val plannedDeparturePlatform: String? = "",
    val departurePrognosisType: String? = "",
)

data class Origin(
    val type: String? = "",
    val id: String? = "",
    val name: String? = "",
    val location: Location? = null,
    val products: Products? = null,
)

data class Destination(
    val type: String,
    val id: String,
    val name: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val poi: Boolean,
)

data class Line(
    val type: String,
    val id: String,
    val fahrtNr: Int,
    val name: String,
    val public: Boolean,
    val adminCode: String,
    val mode: String,
    val product: String,
    val operator: Operator,
)

data class Operator(
    val type: String,
    val id: String,
    val name: String,
)

data class CurrentLocation(
    val type: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
)

data class Products(
    val suburban: Boolean,
    val subway: Boolean,
)
