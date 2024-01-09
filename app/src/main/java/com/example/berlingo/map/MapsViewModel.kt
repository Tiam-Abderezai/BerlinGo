package com.example.berlingo.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlingo.common.API_KEY_GOOGLE_MAPS
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.maps.MapsNetworkApiImpl
import com.example.berlingo.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MapsViewModel::class)

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapsNetworkApiImpl: MapsNetworkApiImpl,
) : ViewModel() {
    private val _state = MutableStateFlow(MapsState())
    val state = _state.asStateFlow()

    fun handleEvent(event: MapsEvent) {
        when (event) {
            is MapsEvent.DirectionsGet -> {
                viewModelScope.launch {
                    getDirection(event.origin, event.destination, event.mode, event.transitMode, event.language)
                }
            }
        }
    }

    private suspend fun getDirection(
        origin: String,
        destination: String,
        mode: String,
        transitMode: String,
        language: String,
    ) {
        val directions = mapsNetworkApiImpl.getDirection(
            key = API_KEY_GOOGLE_MAPS,
            origin = origin,
            destination = destination,
            mode = mode,
            transitMode = transitMode,
            language = language,
        )
        logger.debug("getDirection message:${directions.message}")
        logger.debug("getDirection data:${directions.data} ")

        _state.value = MapsState(routes = directions.data?.routes)
    }
}
