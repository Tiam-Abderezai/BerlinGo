package com.example.berlingo.data.network

import android.util.Log
import com.example.berlingo.data.network.responses.JourneysResponse
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.data.network.responses.Trip
import com.example.berlingo.data.network.responses.TripResponse
import com.example.berlingo.data.network.responses.TripsResponse
import com.example.berlingo.repositories.AppRepository
import com.google.gson.JsonObject
import javax.inject.Inject

class NetworkApiImpl @Inject constructor(
    private val networkApi: NetworkApi,
) : AppRepository {

    override suspend fun getLocations(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>> {
        return try {
            val response = networkApi.getLocations(poi = poi, addresses = addresses, query = query)
            Log.d("dev-log", "getLocations: ${response.raw()} ")
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
            Resource.error("${e.message}", null)
        }
    }

    override suspend fun getJourneys(from: String, toId: String, toLatitude: Double, toLongitude: Double): Resource<JourneysResponse> {
        return try {
            val response = networkApi.getJourneys(from = from, toId = toId, toLatitude = toLatitude, toLongitude = toLongitude)
//            Log.d("dev-log", "getJourneys: ${response.raw()} ")

            if (response.isSuccessful) {
                Log.d("dev-log", "getJourneys: ${response.raw().body} ")

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
            Resource.error("${e.message}", null)
        }
    }

    override suspend fun getTrips(from: String, to: String, results: Int): Resource<TripsResponse> {
        return try {
            val response = networkApi.getTrips(from = from, to = to, results = 10)
            Log.d("dev-log", "getTrips: ${response.raw()} ")

            if (response.isSuccessful) {
                Log.d("dev-log", "getTrips: ${response.raw().body} ")

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
            Resource.error("${e.message}", null)
        }
    }

    override suspend fun getTripById(tripId: String): Resource<TripResponse> {
        return try {
            val response = networkApi.getTripById(tripId = tripId)
            Log.d("dev-log", "getTrips: ${response.raw()} ")

            if (response.isSuccessful) {
                Log.d("dev-log", "getTrips: ${response.raw().body} ")

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
            Resource.error("${e.message}", null)
        }
    }

    override suspend fun getStopsByDeparture(stopId: String, direction: String, duration: Int): Resource<Stop> {
        return try {
            val response = networkApi.getStopsByDeparture(stopId = stopId, direction = direction, duration = duration)
            Log.d("dev-log", "getTrips: ${response.raw()} ")

            if (response.isSuccessful) {
                Log.d("dev-log", "getTrips: ${response.raw().body} ")

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
            Resource.error("${e.message}", null)
        }
    }
}
