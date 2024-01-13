package com.example.berlingo.data.network.maps.responses

import com.google.gson.annotations.SerializedName

data class DirectionsResponse(
    @SerializedName("geocoded_waypoints") val geocodedWaypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
)

data class GeocodedWaypoint(
    @SerializedName("geocoder_status") val geocoderStatus: String,
    @SerializedName("place_id") val placeId: String,
    val types: List<String>,
)

data class Route(
    val bounds: Bounds? = Bounds(),
    val copyrights: String? = "",
    val legs: List<Leg>? = emptyList(),
    @SerializedName("overview_polyline")
    val polyline: Polyline? = Polyline(),
)

data class Bounds(
    val northeast: Location? = Location(),
    val southwest: Location? = Location(),
)

data class Location(
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
)

data class Leg(
    @SerializedName("arrival_time") val arrivalTime: Time,
    @SerializedName("departure_time") val departureTime: Time,
    val distance: DistanceDuration,
    val duration: DistanceDuration,
    @SerializedName("end_address") val endAddress: String,
    @SerializedName("end_location") val endLocation: Location,
    @SerializedName("start_address") val startAddress: String,
    @SerializedName("start_location") val startLocation: Location,
    val steps: List<Step>,
)

data class Time(
    val text: String,
    @SerializedName("time_zone") val timeZone: String,
    val value: Int,
)

data class DistanceDuration(
    val text: String,
    val value: Int,
)

data class Step(
    val distance: DistanceDuration,
    val duration: DistanceDuration,
    @SerializedName("end_location") val endLocation: Location,
    @SerializedName("html_instructions") val htmlInstructions: String,
    val polyline: Polyline,
    @SerializedName("start_location") val startLocation: Location,
)

data class Polyline(
    val points: String? = "",
)
