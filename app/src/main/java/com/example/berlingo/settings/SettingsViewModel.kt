package com.example.berlingo.settings

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.network.JourneysApiImpl
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.journeys.network.responses.Remark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(SettingsViewModel::class)

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<SettingsState>(SettingsState.Initial)
    val state: StateFlow<SettingsState> = _state.asStateFlow()

}
