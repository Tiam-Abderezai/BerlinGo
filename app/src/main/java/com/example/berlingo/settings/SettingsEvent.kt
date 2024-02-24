package com.example.berlingo.settings

import java.util.Locale

sealed class SettingsEvent {
    data class SaveLanguageSettings(
        val locale: Locale,
    ) : SettingsEvent()
    data class SaveDarkModeSettings(
        val darkMode: Boolean,
    ) : SettingsEvent()
}
