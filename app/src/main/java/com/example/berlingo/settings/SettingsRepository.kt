package com.example.berlingo.settings

import android.content.SharedPreferences
import java.util.Locale
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveLanguageSettings(locale: Locale) {
        sharedPreferences.edit().putString("settings_language", locale.language).apply()
    }

    fun getLanguageSettings(): String? {
        return sharedPreferences.getString("settings_language", null)
    }
}