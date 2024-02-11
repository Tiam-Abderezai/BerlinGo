package com.example.berlingo.settings.app_info

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions
import com.example.berlingo.common.components.Divider
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import java.util.Locale
import java.util.TimeZone

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("AppInfoSettingsScreen()")

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppInfoSettingsScreen(navController: NavHostController) {
    val textColor = if (isSystemInDarkTheme()) LightGray else DarkGray
    val backgroundColor = if (isSystemInDarkTheme()) DarkGray else LightGray
    Scaffold(topBar = { SetTopAppBar(navController, backgroundColor) }) {
        Surface(color = backgroundColor, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(Dimensions.large))
                Divider()
                AppVersion(textColor)
                Divider()
                DeviceModel(textColor)
                Divider()
                SystemVersion(textColor)
                Divider()
                PlayStoreVersion(textColor)
                Divider()
                DeviceLanguage(textColor)
                Divider()
                DeviceRegion(textColor)
                Divider()
                DeviceTimeZone(textColor)
                Divider()
            }
        }
    }
}

@Composable
fun AppVersion(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${stringResource(R.string.app_version)}: ",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Beta",
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DeviceModel(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column() {
            Row() {
                Text(
                    text = "${stringResource(R.string.device_model)}: ",
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = Build.MODEL,
                    color = textColor,
                    fontSize = 16.sp,
                )
            }
            Row() {
                Text(
                    text = "${stringResource(R.string.device_brand)}: ",
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = Build.BRAND,
                    color = textColor,
                    fontSize = 16.sp,
                )
            }
            Row() {
                Text(
                    text = "${stringResource(R.string.device_manufacturer)}: ",
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = Build.MANUFACTURER,
                    color = textColor,
                    fontSize = 16.sp,
                )
            }
            Row() {
                Text(
                    text = "${stringResource(R.string.device)}: ",
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = Build.DEVICE,
                    color = textColor,
                    fontSize = 16.sp,
                )
            }
            Row() {
                Text(
                    text = "${stringResource(R.string.device_product)}: ",
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = Build.PRODUCT,
                    color = textColor,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Composable
fun SystemVersion(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${stringResource(R.string.system_version)}: ",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = Build.VERSION.SDK_INT.toString(),
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun PlayStoreVersion(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${stringResource(R.string.playstore_version)}: ",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Beta",
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DeviceLanguage(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${stringResource(R.string.language)}: ",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = Locale.getDefault().language,
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DeviceRegion(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${stringResource(R.string.region)}: ",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = Locale.getDefault().country,
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DeviceTimeZone(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${stringResource(R.string.time_zone)}: ",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = TimeZone.getDefault().id,
            color = textColor,
            fontSize = 16.sp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetTopAppBar(navController: NavHostController, backgroundColor: Color) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_info)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(backgroundColor),
    )
}