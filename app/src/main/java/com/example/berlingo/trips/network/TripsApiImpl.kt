package com.example.berlingo.trips.network

import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.Resource
import com.example.berlingo.trips.network.responses.TripResponse
import com.example.berlingo.trips.network.responses.TripsResponse
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(TripsApiImpl::class)

class TripsApiImpl @Inject constructor(
    private val tripsApi: TripsApi,
) : TripsRepository {

    override suspend fun getTrips(from: String, to: String, results: Int): Resource<TripsResponse> {
        return try {
            val response = tripsApi.getTrips(from = from, to = to, results = 10)
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
            val response = tripsApi.getTripById(tripId = tripId)
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

}