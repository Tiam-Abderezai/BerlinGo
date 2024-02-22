package com.example.berlingo.map.network

import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.Resource
import com.example.berlingo.map.network.responses.DirectionsResponse
import com.example.berlingo.map.MapsRepository
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MapsApiImpl::class)

class MapsApiImpl @Inject constructor(
    private val mapsApi: MapsApi,
) : MapsRepository {

    override suspend fun getDirection(key: String, origin: String, destination: String, mode: String, transitMode: String, language: String): Resource<DirectionsResponse> {
        return try {
            val response = mapsApi.getDirection(key = key, origin = origin, destination = destination, mode = mode, transitMode = transitMode, language = language)
            logger.debug("getDirection message: ${response.raw()} ")

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
}
