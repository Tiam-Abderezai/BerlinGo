package com.example.berlingo.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store_settings")

@Singleton
class SettingsRepository @Inject constructor(private val context: Context) {
    private val localeKey = stringPreferencesKey("locale")
    private val darkModeKey = booleanPreferencesKey("dark_mode")

    private val _languageState = MutableStateFlow(getLanguageSettings())
    val languageState: StateFlow<String> = _languageState
    private val _darkModeState = MutableStateFlow(getDarkModeSettings())
    val darkModeState: StateFlow<Boolean> = _darkModeState

    private fun getLanguageSettings(): String = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[localeKey] ?: Locale.getDefault().language
        }.first()
    }

    suspend fun setLanguageSettings(locale: String) {
        context.dataStore.edit { preferences ->
            preferences[localeKey] = locale
        }
        _languageState.value = locale // Update StateFlow
    }
    suspend fun setDarkModeSettings(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[darkModeKey] = isDarkMode
        }
        _darkModeState.value = isDarkMode // Update StateFlow
    }

    private fun getDarkModeSettings(): Boolean = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[darkModeKey] ?: false
        }.first()
    }
}
