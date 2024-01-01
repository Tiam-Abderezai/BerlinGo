package com.example.berlingo.routes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlingo.data.network.NetworkApiImpl
import com.example.berlingo.data.network.responses.Stop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val networkApiImpl: NetworkApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow(RoutesViewState())
    val state = _state.asStateFlow()

    private val _stateTextOrigin = MutableStateFlow("")
    val stateTextOrigin: StateFlow<String> = _stateTextOrigin

    private val _stateTextDestination = MutableStateFlow("")
    val stateTextDestination: StateFlow<String> = _stateTextDestination

    private val _searchLocations = MutableStateFlow<List<Stop>>(emptyList())
    val searchLocations: StateFlow<List<Stop>> = _searchLocations

//    private val _searchJourneys = MutableStateFlow<List<String>>(emptyList())
//    val searchJourneys: StateFlow<List<String>> = _searchJourneys

    private val _selectedLocation = MutableStateFlow("")
    val selectedLocation: StateFlow<String> = _selectedLocation

    private val _searchedJourneys = MutableStateFlow<List<String>>(emptyList())
    val searchedJourneys: StateFlow<List<String>> = _searchedJourneys

    init {
        viewModelScope.launch {
//            val journeys = networkApiImpl.getJourneys()
//            val locations = networkApiImpl.getLocations(false, addresses = false, query = "")

//            Log.d("dev-log", "data ${journeys}")
//            Log.d("dev-log", "message ${journeys.message}")
//            Log.d("dev-log", "status ${journeys.status}")

//            Log.d("dev-log", "data${locations}")
//            Log.d("dev-log", "message ${locations.message}")
//            Log.d("dev-log", "status ${locations.status}")

//            _state.update { it.copy(isLoading = true) }
//            _state.update {
//                it.copy(
//                    repositories = appRepository.getLocations(""),
//                    isLoading = true,
//                )
//            }
        }
    }

    suspend fun queryLocation(query: String) {
        val locations = networkApiImpl.getLocations(false, addresses = false, query = query)

        _searchLocations.value = if (query.isEmpty()) {
            emptyList()
        } else {
            locations.data?.filter {
                Log.d("dev-log", "id: ${it.id} name: ${it.name} long: ${it.location.longitude} lat: ${it.location.latitude}")
                it.name.contains(query, ignoreCase = true)
            } ?: emptyList()
        }
    }

    suspend fun queryJourneys(from: String, toId: String, toName: String, toLatitude: Double, toLongitude: Double) {
        val journeys = networkApiImpl.getJourneys(from = from, toId = toId, toName = toName, toLatitude = toLatitude, toLongitude = toLongitude)
        Log.d("dev-log", "journeys data: ${journeys.data?.journeys?.get(0)?.legs}")
        Log.d("dev-log", "journeys message: ${journeys.message}")

        _searchedJourneys.value = journeys.data?.journeys?.get(0)?.legs?.map {
            "${it.origin.name} to ${it.destination.name}"
        }!! //TODO replace !! with a safer alternative
//        _searchJourneys.value = if (from.isEmpty()) {
//            emptyList()
//        } else {
//            journeys.data?.asJsonArray?.filter {
// //                Log.d("dev-log", "querySearch: ${it.name}")
//
// //                it.asString.contains(query, ignoreCase = true)
//            } ?: emptyList()
//        }
    }

    fun selectLocation(location: String) {
        _selectedLocation.value = location
    }
}
