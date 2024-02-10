package com.example.berlingo.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions
import com.example.berlingo.common.components.Divider
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("SettingsScreen()")

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SettingsScreen() {
    val textColor = if (isSystemInDarkTheme()) LightGray else DarkGray
    val backgroundColor = if (isSystemInDarkTheme()) DarkGray else LightGray
    Surface(color = backgroundColor, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Divider()
            AppInfo(textColor)
            Divider()
            LanguageSettings(textColor)
            Divider()
            DarkModeSettings(textColor)
            Divider()
            InformationPrivacy(textColor)
            Divider()
        }
    }
}

@Composable
fun AppInfo(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(Dimensions.medium),
            painter = painterResource(id = R.drawable.icon_app_info),
            contentDescription = stringResource(R.string.app_info),
        )
        Spacer(modifier = Modifier.width(Dimensions.smallXX))
        Text(
            text = stringResource(R.string.app_info),
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun LanguageSettings(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(Dimensions.medium),
            painter = painterResource(id = R.drawable.icon_language),
            contentDescription = stringResource(R.string.language_settings),
        )
        Spacer(modifier = Modifier.width(Dimensions.smallXX))
        Text(
            text = stringResource(R.string.language_settings),
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DarkModeSettings(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(Dimensions.medium),
            painter = painterResource(id = R.drawable.icon_dark_mode),
            contentDescription = stringResource(R.string.dark_mode),
        )
        Spacer(modifier = Modifier.width(Dimensions.smallXX))
        Text(
            text = stringResource(R.string.dark_mode),
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun InformationPrivacy(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(Dimensions.medium),
            painter = painterResource(id = R.drawable.icon_privacy),
            contentDescription = stringResource(R.string.information_privacy),
        )
        Spacer(modifier = Modifier.width(Dimensions.smallXX))
        Text(
            text = stringResource(R.string.information_privacy),
            color = textColor,
            fontSize = 16.sp,
        )
    }
}
