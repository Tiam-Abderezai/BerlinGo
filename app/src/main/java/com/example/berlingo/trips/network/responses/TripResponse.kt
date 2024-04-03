package com.example.berlingo.trips.network.responses

data class TripsResponse(
    val trips: List<Trip>,
)

data class TripResponse(
    val trip: Trip,
)

data class Trip(
    val arrival: String? = "",
    val arrivalDelay: Any? = null,
    val arrivalPlatform: Any? = null,
    val arrivalPrognosisType: Any? = null,
    val currentLocation: CurrentLocation? = null,
    val departure: String? = "",
    val departureDelay: Any? = null,
    val departurePlatform: Any? = null,
    val departurePrognosisType: Any? = null,
    val destination: Destination? = null,
    val direction: Any? = null,
    val id: String? = "",
    val line: Line? = null,
    val origin: Origin? = null,
    val plannedArrival: String? = "",
    val plannedArrivalPlatform: Any? = null,
    val plannedDeparture: String? = "",
    val plannedDeparturePlatform: Any? = null,
    val stopovers: List<Stopover>? = emptyList(),
) {
    data class CurrentLocation(
        val latitude: Double,
        val longitude: Double,
        val type: String,
    )

    data class Destination(
        val id: String? = "",
        val location: Location? = null,
        val name: String? = "",
        val products: Products? = null,
        val stationDHID: String? = "",
        val type: String? = "",
    ) {
        data class Location(
            val id: String,
            val latitude: Double,
            val longitude: Double,
            val type: String,
        )

        data class Products(
            val bus: Boolean,
            val express: Boolean,
            val ferry: Boolean,
            val regional: Boolean,
            val suburban: Boolean,
            val subway: Boolean,
            val tram: Boolean,
        )
    }

    data class Line(
        val adminCode: String,
        val fahrtNr: String,
        val id: String,
        val mode: String,
        val name: String,
        val `operator`: Operator,
        val product: String,
        val productName: String,
        val `public`: Boolean,
        val type: String,
    ) {
        data class Operator(
            val id: String,
            val name: String,
            val type: String,
        )
    }

    data class Origin(
        val id: String? = "",
        val location: Location? = null,
        val name: String? = "",
        val products: Products? = null,
        val stationDHID: String? = "",
        val type: String? = "",
    ) {
        data class Location(
            val id: String? = "",
            val latitude: Double? = 0.0,
            val longitude: Double? = 0.0,
            val type: String? = "",
        )

        data class Products(
            val bus: Boolean,
            val express: Boolean,
            val ferry: Boolean,
            val regional: Boolean,
            val suburban: Boolean,
            val subway: Boolean,
            val tram: Boolean,
        )
    }

    data class Stopover(
        val arrival: String? = "",
        val arrivalDelay: Any? = null,
        val arrivalPlatform: Any? = null,
        val arrivalPrognosisType: Any? = null,
        val departure: String? = "",
        val departureDelay: Any? = null,
        val departurePlatform: Any? = null,
        val departurePrognosisType: Any? = null,
        val plannedArrival: String? = "",
        val plannedArrivalPlatform: Any? = null,
        val plannedDeparture: String? = "",
        val plannedDeparturePlatform: Any? = null,
        val stop: Stop? = null,
    ) {
        data class Stop(
            val id: String,
            val location: Location,
            val name: String,
            val products: Products,
            val stationDHID: String,
            val type: String,
        ) {
            data class Location(
                val id: String,
                val latitude: Double,
                val longitude: Double,
                val type: String,
            )

            data class Products(
                val bus: Boolean,
                val express: Boolean,
                val ferry: Boolean,
                val regional: Boolean,
                val suburban: Boolean,
                val subway: Boolean,
                val tram: Boolean,
            )
        }
    }
}
