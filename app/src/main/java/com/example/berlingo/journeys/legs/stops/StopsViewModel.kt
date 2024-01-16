package com.example.berlingo.journeys.legs.stops

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.legs.stops.network.StopsApiImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(StopsViewModel::class)

@HiltViewModel
class StopsViewModel @Inject constructor(
    private val stopsApiImpl: StopsApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow<StopsState>(StopsState.Initial)
    val state: StateFlow<StopsState> = _state.asStateFlow()

    suspend fun handleEvent(event: StopsEvent) {
        when (event) {
            is StopsEvent.StopsQueryEvent -> {
                queryStops(event.name)
            }
        }
    }

    private suspend fun queryStops(query: String) {
        try {
            _state.value = StopsState.Loading
            val stops = stopsApiImpl.getStops(false, addresses = false, query = query).data
            if (query.isNotEmpty()) {
                stops?.filter { stop ->
                    stop.name?.contains(query, ignoreCase = true) ?: false
                }
            }
            _state.value = StopsState.Success(data = stops ?: emptyList())
        } catch (e: Exception) {
            _state.value = StopsState.Error(e.message ?: "Unknown Error")
        }
    }

//    private suspend fun queryStops(query: String) {
//        val stops = stopsApiImpl.getStops(false, addresses = false, query = query).data
//        if (query.isEmpty()) {
//            emptyList()
//        } else {
//            stops?.filter { stop ->
//                stop.name?.contains(query, ignoreCase = true) ?: false
//            } ?: emptyList()
//        }
//        _state.value = StopsState(data = stops)
//    }
}
