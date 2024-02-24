package com.example.berlingo.settings

sealed class SettingsState {
    data class Initial(
        val language: String = "",
        val darkMode: Boolean = false,
    ) : SettingsState()
    object Loading : SettingsState()
    data class Success(
        val language: String = "",
        val darkMode: Boolean = false,
    ) : SettingsState()
    data class Error(val message: String) : SettingsState()

//    data class Warning(val remarks: List<Remark>)
}
