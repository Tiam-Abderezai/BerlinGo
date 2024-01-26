package com.example.berlingo.journeys.network.responses

import com.example.berlingo.journeys.legs.stops.network.responses.Location
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.OffsetDateTime

data class JourneysResponse(
    val journeys: List<Journey>,
)

data class Journey(
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
    val line: Line? = Line(),
    val direction: String? = "",
    val currentLocation: CurrentLocation? = null,
    val arrivalPlatform: String? = "",
    val plannedArrivalPlatform: String? = "",
    val arrivalPrognosisType: String? = "",
    val departurePlatform: String? = "",
    val plannedDeparturePlatform: String? = "",
    val departurePrognosisType: String? = "",
    val cancelled: Boolean? = false,
    val remarks: List<Remark>? = emptyList()
)

data class Origin(
    val type: String? = "",
    val id: String? = "",
    val name: String? = "",
    val location: Location? = null,
    val products: Products? = null,
)

data class Destination(
    val type: String? = "",
    val id: String? = "",
    val name: String? = "",
    val latitude: BigDecimal? = BigDecimal.ZERO,
    val longitude: BigDecimal? = BigDecimal.ZERO,
    val poi: Boolean? = false,
)

data class Line(
    val type: String? = "",
    val id: String? = "",
    val fahrtNr: Int? = 0,
    val name: String? = "",
    val public: Boolean? = false,
    val adminCode: String? = "",
    val mode: String? = "",
    val product: String? = "",
    val operator: Operator? = Operator(),
)

data class Operator(
    val type: String? = "",
    val id: String? = "",
    val name: String? = "",
)

data class CurrentLocation(
    val type: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
)

data class Products(
    val suburban: Boolean,
    val subway: Boolean,
    val tram: Boolean,
    val bus: Boolean,
    val ferry: Boolean,
    val express: Boolean,
    val regional: Boolean,
)
data class Remark(
    val id: String? = "",
    val type: String? = "",
    val code: String? = "",
    val text: String? = "",
    val summary: String? = "",
    val icon: Icon? = Icon(),
    val priority: Int? = 0,
    val products: Products? = null,
    val company: String? = "",
    val category: Categories? = null,
    val validFrom: String? = "",
    val validUntil: String? = "",
    val modified: String? = "",
)

data class Categories(
    @SerializedName("0")
    val zero: Int? = 0,
)
data class Icon(
    val type: String? = "",
    val title: String? = "",
)
