package com.example.berlingo.journeys.network

import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.Resource
import com.example.berlingo.journeys.network.responses.JourneysResponse
import java.util.Locale
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(JourneysRepositoryImpl::class)

class JourneysRepositoryImpl @Inject constructor(
    private val journeysApi: JourneysApi,
) : JourneysRepository {

    override suspend fun getJourneys(from: String, toId: String, toLatitude: String, toLongitude: String): Resource<JourneysResponse> {
        return try {
            val response = journeysApi.getJourneys(from = from, toId = toId, toLatitude = toLatitude, toLongitude = toLongitude)

            logger.debug("getJourneys: ${response.raw()}")

            if (response.isSuccessful) {
                logger.debug("Locale.getDefault().language: ${Locale.getDefault().language}")
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
}
