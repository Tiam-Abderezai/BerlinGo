//package com.example.berlingo.trips
//
//import androidx.lifecycle.ViewModel
//import com.example.berlingo.common.logger.BaseLogger
//import com.example.berlingo.common.logger.FactoryLogger
//import com.example.berlingo.journeys.JourneysEvent
//import com.example.berlingo.journeys.JourneysState
//import com.example.berlingo.journeys.JourneysViewModel
//import com.example.berlingo.journeys.logger
//import com.example.berlingo.journeys.network.JourneysApiImpl
//import com.example.berlingo.journeys.network.responses.Journey
//import com.example.berlingo.trips.network.responses.Trip
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import javax.inject.Inject
//
//private val logger: BaseLogger = FactoryLogger.getLoggerKClass(TripsViewModel::class)
//
//@HiltViewModel
//class TripsViewModel @Inject constructor(
//    private val journeysApiImpl: JourneysApiImpl,
//) : ViewModel() {
//    private val _state = MutableStateFlow<TripsState<List<Trip>>>(TripsState.Loading)
//    val state: StateFlow<TripsState<List<Trip>>> = _state.asStateFlow()
//
//
//    suspend fun handleEvent(event: TripsEvent) {
//        when (event) {
//            is JourneysEvent.TripQueryEvent -> {
//                queryTripById(event.tripId)
//            }
//        }
//    }
//
//    suspend fun queryTrips(from: String, to: String, results: Int) {
//        val trips = journeysApiImpl.getTrips(from = from, to = to, results)
////        _searchedTrips.value = trips.data?.trips ?: emptyList()
//    }
//
//    private suspend fun queryTripById(tripId: String) {
//        val trip = journeysApiImpl.getTripById(tripId = tripId).data?.trip
//        logger.debug("stopovers stop: $trip")
//
//        _state.value = JourneysState.Success(trip)
//    }
//}