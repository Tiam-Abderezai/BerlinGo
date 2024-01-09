package com.example.berlingo.data.network.maps

import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.Resource
import com.example.berlingo.data.network.responses.maps.DirectionsResponse
import com.example.berlingo.repositories.MapsRepository
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MapsNetworkApiImpl::class)

class MapsNetworkApiImpl @Inject constructor(
    private val mapsNetworkApi: MapsNetworkApi,
) : MapsRepository {

    override suspend fun getDirection(key: String, origin: String, destination: String, mode: String, transitMode: String, language: String): Resource<DirectionsResponse> {
        return try {
            val response = mapsNetworkApi.getDirection(key = key, origin = origin, destination = destination, mode = mode, transitMode = transitMode, language = language)
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
