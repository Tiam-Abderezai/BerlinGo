package com.example.berlingo.settings.language

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleUtils {
    fun updateLocale(language: String, context: Context): Locale {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        return locale
    }
}