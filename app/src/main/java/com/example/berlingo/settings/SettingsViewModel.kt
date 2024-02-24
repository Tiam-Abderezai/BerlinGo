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
class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository) :
    ViewModel() {
    private val darkModeSettingsState = settingsRepository.darkModeState
    private val languageSettingsState = settingsRepository.languageState
    private val _state = MutableStateFlow<SettingsState>(SettingsState.Initial(languageSettingsState.value, darkModeSettingsState.value))
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        // init block used so MainActivity can check current app language settings when started
        if (languageSettingsState.value.isNotEmpty()) {
            _state.value = SettingsState.Success(languageSettingsState.value, darkModeSettingsState.value)
        }
    }

    suspend fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SaveLanguageSettings -> saveLanguageSettings(event.locale)
            is SettingsEvent.SaveDarkModeSettings -> saveDarkModeSettings(event.darkMode)
        }
    }

    private suspend fun saveLanguageSettings(
        locale: Locale,
    ) {
        try {
            _state.value = SettingsState.Loading
            settingsRepository.setLanguageSettings(locale.language)
            _state.value = SettingsState.Success(language = languageSettingsState.value)
        } catch (e: Exception) {
            _state.value = SettingsState.Error(message = e.message ?: "Unknown Error")
        }
    }

    private suspend fun saveDarkModeSettings(
        darkMode: Boolean,
    ) {
        try {
            _state.value = SettingsState.Loading
            settingsRepository.setDarkModeSettings(darkMode)
            _state.value = SettingsState.Success(darkMode = darkModeSettingsState.value)
        } catch (e: Exception) {
            _state.value = SettingsState.Error(message = e.message ?: "Unknown Error")
        }
    }
}
