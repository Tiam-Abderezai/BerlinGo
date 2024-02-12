package com.example.berlingo.settings

sealed class SettingsState {
    data class Initial(
        val data: String,
    ) : SettingsState()
    object Loading : SettingsState()
    data class Success(
        val data: String,
    ) : SettingsState()
    data class Error(val message: String) : SettingsState()

//    data class Warning(val remarks: List<Remark>)
}
