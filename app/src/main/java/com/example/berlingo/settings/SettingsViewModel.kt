package com.example.berlingo.settings

import androidx.lifecycle.ViewModel
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(SettingsViewModel::class)

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _state = MutableStateFlow<SettingsState>(SettingsState.Initial(settingsRepository.getLanguageSettings() ?: Locale.getDefault().language))
    val state: StateFlow<SettingsState> = _state.asStateFlow()
    private val languageRegionCode = settingsRepository.getLanguageSettings()
    init {
        // init block used so MainActivity can check current app language settings when started
        if (!languageRegionCode.isNullOrEmpty()) {
            _state.value = SettingsState.Success(languageRegionCode)
        }
    }
    suspend fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SaveLanguageSettings -> saveLanguageSettings(event.locale)
        }
    }

    private suspend fun saveLanguageSettings(
        locale: Locale,
    ) {
        try {
            _state.value = SettingsState.Loading
            settingsRepository.saveLanguageSettings(locale)
            _state.value = SettingsState.Success(settingsRepository.getLanguageSettings() ?: "")
        } catch (e: Exception) {
            _state.value = SettingsState.Error(message = e.message ?: "Unknown Error")
        }
    }
}
