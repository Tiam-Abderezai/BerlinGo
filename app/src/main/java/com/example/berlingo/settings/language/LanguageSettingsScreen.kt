package com.example.berlingo.settings.language

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions
import com.example.berlingo.common.components.Divider
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.settings.SettingsEvent
import com.example.berlingo.settings.SettingsManager
import com.example.berlingo.settings.SettingsState
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightBlue
import com.example.berlingo.ui.theme.LightGray
import com.example.berlingo.ui.theme.isDarkMode
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
        is SettingsState.Initial -> LanguageSettings(
            navController,
            settingsState.language,
            settingsEvent,
        )

        is SettingsState.Loading -> LoadingScreen()
        is SettingsState.Error -> ErrorScreen(message = settingsState.message)
        is SettingsState.Success -> LanguageSettings(
            navController,
            settingsState.language,
            settingsEvent,
        )
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
    val textColor = if (isDarkMode()) LightGray else DarkGray
    val backgroundColor = if (isDarkMode()) DarkGray else LightGray
    Scaffold(topBar = { SetTopAppBar(navController, backgroundColor, textColor) }) {
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
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.large),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue,
            ),
            shape = RoundedCornerShape(Dimensions.smallXXX),
        ) {
            Text(text = stringResource(id = selectedOption), color = textColor, fontSize = 32.sp)
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val lang = getLanguageCode(language)
                                val locale = SettingsManager().updateLanguage(lang, context)
                                saveLocale(settingsEvent, locale)
                                selectedOption = language
                            },
                    ) {
                        Text(text = stringResource(id = language), color = textColor, fontSize = 32.sp)
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

private fun getLanguageCode(id: Int): String {
    return when (id) {
        R.string.locale_en -> "en"
        R.string.locale_de -> "de"
        R.string.locale_fr -> "fr"
        else -> "en"
    }
}

private fun saveLocale(settingsEvent: suspend (event: SettingsEvent) -> Unit, locale: Locale) {
    CoroutineScope(Dispatchers.IO).launch {
        settingsEvent.invoke(SettingsEvent.SaveLanguageSettings(locale))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetTopAppBar(
    navController: NavHostController,
    backgroundColor: Color,
    textColor: Color,
) {
    logger.debug("SetTopAppBar: ${stringResource(R.string.language)}")
    TopAppBar(
        title = { Text(stringResource(R.string.language), color = textColor) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(backgroundColor),
    )
}
