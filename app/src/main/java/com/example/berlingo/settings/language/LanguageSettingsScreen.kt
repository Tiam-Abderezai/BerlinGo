package com.example.berlingo.settings.language

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import com.example.berlingo.R
import com.example.berlingo.common.components.Divider
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.settings.SettingsEvent
import com.example.berlingo.settings.SettingsState
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("LanguageSettingsScreen()")

val dropdownLanguages = listOf(R.string.locale_en, R.string.locale_de, R.string.locale_fr)

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LanguageSettingsScreen(
    navController: NavHostController,
    settingsState: SettingsState,
    settingsEvent: suspend (event: SettingsEvent) -> Unit,
) {
    when (settingsState) {
        is SettingsState.Initial -> LanguageSettings(navController, settingsState.data, settingsEvent)
        is SettingsState.Loading -> LoadingScreen()
        is SettingsState.Error -> ErrorScreen(message = settingsState.message)
        is SettingsState.Success -> LanguageSettings(navController, settingsState.data, settingsEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LanguageSettings(
    navController: NavHostController,
    language: String,
    settingsEvent: suspend (event: SettingsEvent) -> Unit,
) {
    val textColor = if (isSystemInDarkTheme()) LightGray else DarkGray
    val backgroundColor = if (isSystemInDarkTheme()) DarkGray else LightGray
    Scaffold(topBar = { SetTopAppBar(navController, language, backgroundColor) }) {
        Surface(color = backgroundColor, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(200.dp))
                Divider()
                LanguageDropdownMenu(
                    language,
                    settingsEvent,
                    textColor,
                    backgroundColor,
                )
                Divider()
            }
        }
    }
}

@Composable
fun LanguageDropdownMenu(
    language: String,
    settingsEvent: suspend (event: SettingsEvent) -> Unit,
    textColor: Color,
    backgroundColor: Color,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) } // State for the dropdown menu visibility
    logger.debug("selectedOption: $language")
    var selectedOption by remember { mutableStateOf(getLanguageStringResource(language)) } // State for the selected option

    Column() {
        Button(onClick = { expanded = true }) {
            Text(text = stringResource(id = selectedOption), color = textColor)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor),
        ) {
            dropdownLanguages.forEach { language ->
                DropdownMenuItem(onClick = {
                    expanded = false
                }) {
                    logger.debug("locale: ${stringResource(id = language)}")
                    val lang = stringResource(id = language)
                    Row(
                        Modifier.clickable {
//                            val locale = LocaleUtils.updateLocale(language, context)
//                            val locale = AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))

//                            saveLocale(settingsEvent, locale)
                            selectedOption = language
                        },
                    ) {
                        Text(text = stringResource(id = language), color = textColor)
                    }
                }
            }
        }
    }
}

private fun getLanguageStringResource(language: String): Int {
    return when (language) {
        "en" -> R.string.locale_en
        "de" -> R.string.locale_de
        "fr" -> R.string.locale_fr
        else -> R.string.locale_en
    }
}

private fun saveLocale(settingsEvent: suspend (event: SettingsEvent) -> Unit, locale: Locale) {
    CoroutineScope(Dispatchers.IO).launch {
        settingsEvent.invoke(SettingsEvent.SaveLanguageSettings(locale))
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetTopAppBar(navController: NavHostController, language: String, backgroundColor: Color) {
    logger.debug("SetTopAppBar: ${stringResource(R.string.language)}")
    TopAppBar(
        title = { Text(stringResource(R.string.language)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(backgroundColor),
    )
}
