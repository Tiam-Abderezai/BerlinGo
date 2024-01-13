package com.example.berlingo.data.network.journeys

import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.Resource
import com.example.berlingo.data.network.journeys.responses.JourneysResponse
import com.example.berlingo.data.network.journeys.responses.Stop
import com.example.berlingo.data.network.journeys.responses.TripResponse
import com.example.berlingo.data.network.journeys.responses.TripsResponse
import com.example.berlingo.repositories.JourneysRepository
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(JourneysApiImpl::class)

class JourneysApiImpl @Inject constructor(
    private val journeysApi: JourneysApi,
) : JourneysRepository {

    override suspend fun getStops(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>> {
        return try {
            val response = journeysApi.getStops(poi = poi, addresses = addresses, query = query)
            logger.debug("getStops: ${response.raw()} ")

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
            val response = journeysApi.getJourneys(from = from, toId = toId, toLatitude = toLatitude, toLongitude = toLongitude)
            logger.debug("getJourneys: ${response.raw()}")

            if (response.isSuccessful) {
                logger.debug("getJourneys: ${response.raw().body} ")

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
            val response = journeysApi.getTrips(from = from, to = to, results = 10)
            logger.debug("getTrips: ${response.raw()} ")

            if (response.isSuccessful) {
                logger.debug("getTrips: ${response.raw().body} ")
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
            val response = journeysApi.getTripById(tripId = tripId)
            logger.debug("getTripById: ${response.raw()} ")

            if (response.isSuccessful) {
                logger.debug("getTripById: ${response.raw().body} ")

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
            val response = journeysApi.getStopsByDeparture(stopId = stopId, direction = direction, duration = duration)
            logger.debug("getStopsByDeparture: ${response.raw()} ")

            if (response.isSuccessful) {
                logger.debug("getTrips: ${response.raw().body} ")

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
