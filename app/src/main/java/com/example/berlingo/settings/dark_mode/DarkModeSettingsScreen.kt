package com.example.berlingo.settings.dark_mode

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

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("DarkModeSettingsScreen()")

val dropdownDarkMode = listOf(R.string.dark, R.string.light)

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DarkModeSettingsScreen(
    navController: NavHostController,
    settingsState: SettingsState,
    settingsEvent: suspend (event: SettingsEvent) -> Unit,
) {
    when (settingsState) {
        is SettingsState.Initial -> DarkModeSettings(
            navController,
            settingsState.darkMode,
            settingsEvent,
        )
        is SettingsState.Loading -> LoadingScreen()
        is SettingsState.Error -> ErrorScreen(message = settingsState.message)
        is SettingsState.Success -> DarkModeSettings(
            navController,
            settingsState.darkMode,
            settingsEvent,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DarkModeSettings(
    navController: NavHostController,
    darkMode: Boolean,
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
                DarkModeDropdownMenu(
                    darkMode,
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
fun DarkModeDropdownMenu(
    darkMode: Boolean,
    settingsEvent: suspend (event: SettingsEvent) -> Unit,
    textColor: Color,
    backgroundColor: Color,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val darkModeIntValue = getDarkModeIntValue(darkMode)
    var selectedOption by remember { mutableStateOf(darkModeIntValue) }

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
            dropdownDarkMode.forEach { darkMode ->
                DropdownMenuItem(modifier = Modifier.background(backgroundColor), onClick = {
                    expanded = false
                }) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                SettingsManager().updateDarkMode(getDarkModeBooleanValue(darkMode), context)
                                saveDarkMode(settingsEvent, darkMode)
                                selectedOption = darkMode
                            },
                    ) {
                        Text(text = stringResource(id = darkMode), color = textColor, fontSize = 32.sp)
                    }
                }
            }
        }
    }
}

fun getDarkModeIntValue(darkMode: Boolean) = if (darkMode) R.string.dark else R.string.light

fun getDarkModeBooleanValue(darkMode: Int) = darkMode == R.string.dark

private fun saveDarkMode(settingsEvent: suspend (event: SettingsEvent) -> Unit, darkMode: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val darkModeBooleanValue = getDarkModeBooleanValue(darkMode)
        settingsEvent.invoke(SettingsEvent.SaveDarkModeSettings(darkModeBooleanValue))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetTopAppBar(navController: NavHostController, backgroundColor: Color, textColor: Color) {
    TopAppBar(
        title = { Text(stringResource(R.string.dark_mode), color = textColor) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(backgroundColor),
    )
}
