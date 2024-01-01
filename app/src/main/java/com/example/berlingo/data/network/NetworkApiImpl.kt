package com.example.berlingo.data.network

import android.util.Log
import com.example.berlingo.data.network.responses.JourneysResponse
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.repositories.AppRepository
import com.google.gson.JsonObject
import javax.inject.Inject

class NetworkApiImpl @Inject constructor(
    private val networkApi: NetworkApi,
) : AppRepository {

    override suspend fun getLocations(poi: Boolean, addresses: Boolean, query: String): Resource<List<Stop>> {
        return try {
            val response = networkApi.getLocations(poi = poi, addresses = addresses, query = query)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
            Resource.error("${e.message}", null)
        }
    }

    override suspend fun getJourneys(from: String, toId: String, toName: String, toLatitude: Double, toLongitude: Double): Resource<JourneysResponse> {
        return try {
            val response = networkApi.getJourneys(from = from, toId = toId, toName = toName, toLatitude = toLatitude, toLongitude = toLongitude)
            Log.d("dev-log", "getJourneys: ${response.raw()} ")

            if (response.isSuccessful) {
                Log.d("dev-log", "getJourneys: ${response.raw().body} ")

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
            Resource.error("${e.message}", null)
        }
    }
}
