package com.example.berlingo.settings

sealed class SettingsState {
    object Initial : SettingsState()
    object Loading : SettingsState()
//    data class Success(
//        val journeys: Map<Journey, List<Leg>>,
//        val warningRemark: Remark? = null,
//    ) : SettingsState()
    data class Error(val message: String) : SettingsState()

//    data class Warning(val remarks: List<Remark>)
}
