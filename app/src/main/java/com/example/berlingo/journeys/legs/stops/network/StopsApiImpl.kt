package com.example.berlingo.journeys.legs.stops.network

import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.Resource
import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(StopsApiImpl::class)

class StopsApiImpl @Inject constructor(
    private val stopsApi: StopsApi,
) : StopsRepository {

    override suspend fun getStops(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>> {
        return try {
            val response = stopsApi.getStops(poi = poi, addresses = addresses, query = query)
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
    override suspend fun getNearestStops(latitude: String, longitude: String): Resource<List<Stop>> {
        return try {
            val response = stopsApi.getNearestStops(latitude = latitude, longitude = longitude)
            logger.debug("getNearestStops: ${response.raw()} ")

            if (response.isSuccessful) {
                logger.debug("getNearestStops: ${response.raw().body} ")

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
            val response = stopsApi.getStopsByDeparture(stopId = stopId, direction = direction, duration = duration)
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