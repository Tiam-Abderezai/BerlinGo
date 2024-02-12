package com.example.berlingo.settings.language

import android.content.Context
import android.content.res.Configuration
import com.example.berlingo.R
import java.util.Locale

object LocaleUtils {
    fun updateLocale(language: Int, context: Context): Locale {
        val locale = Locale(getLanguageCode(language))
        Locale.setDefault(locale)
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        return locale
    }
}
private fun getLanguageCode(item: Int): String {
    return when (item) {
        R.string.locale_en -> "en"
        R.string.locale_de -> "de"
        R.string.locale_fr -> "fr"
        else -> {
            "en"
        }
    }
}