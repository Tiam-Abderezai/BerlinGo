package com.example.berlingo.data.network

import android.util.Log
import com.example.berlingo.data.network.responses.LocationResponse
import com.example.berlingo.repositories.AppRepository
import javax.inject.Inject

class NetworkApiImpl @Inject constructor(
    private val networkApi: NetworkApi,
) : AppRepository {

    override suspend fun getLocations(): Resource<List<LocationResponse>> {
        return try {
            val response = networkApi.getLocations("")
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
}
