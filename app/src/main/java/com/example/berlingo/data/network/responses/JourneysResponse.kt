package com.example.berlingo.data.network.responses

import java.math.BigDecimal
import java.time.OffsetDateTime

data class JourneysResponse(
    val journeys: List<Journeys>,
)

data class Journeys(
    val type: String,
    val legs: List<Leg>
)

data class Leg(
    val origin: Origin,
    val destination: Destination,
    val departure: String,
    val plannedDeparture: String,
    val departureDelay: Int,
    val arrival: String,
    val reachable: Boolean,
    val tripId: String,
    val line: Line,
    val direction: String,
    val currentLocation: CurrentLocation,
    val arrivalPlatform: String,
    val plannedArrivalPlatform: String,
    val arrivalPrognosisType: String,
    val departurePlatform: String,
    val plannedDeparturePlatform: String,
    val departurePrognosisType: String,
//    val remarks: Remarks,
    )

data class Origin(
    val type: String,
    val id: String,
    val name: String,
    val location: Location,
    val products: Products
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
    val operator: Operator
)
data class Operator(
    val type: String,
    val id: String,
    val name: String
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
//data class Remarks()