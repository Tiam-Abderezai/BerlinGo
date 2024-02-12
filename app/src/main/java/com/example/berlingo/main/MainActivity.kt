package com.example.berlingo.main

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.berlingo.R
import com.example.berlingo.Screen
import com.example.berlingo.common.Dimensions.large
import com.example.berlingo.common.Dimensions.smallXXX
import com.example.berlingo.common.Permissions.Companion.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysViewModel
import com.example.berlingo.journeys.legs.stops.StopsViewModel
import com.example.berlingo.map.MapsScreen
import com.example.berlingo.map.MapsViewModel
import com.example.berlingo.routes.JourneysScreen
import com.example.berlingo.settings.SettingsScreen
import com.example.berlingo.settings.SettingsViewModel
import com.example.berlingo.settings.app_info.AppInfoSettingsScreen
import com.example.berlingo.settings.language.LanguageSettingsScreen
import com.example.berlingo.trips.TripsViewModel
import com.example.berlingo.ui.theme.BerlinGoTheme
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightBlue
import com.example.berlingo.ui.theme.LightGray
import dagger.hilt.android.AndroidEntryPoint

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MainActivity::class)
val locationPermissionGranted = mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request User's Location Permission
        getLocationPermission()

        setContent {
            BerlinGoTheme(dynamicColor = false) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) },
                ) {
                    MainNavigationHost(navController = navController)
                }
            }
        }
    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted.value = true // we already have the permission
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted.value = true
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Box() {
        val screens = listOf(Screen.Journeys, Screen.Settings, Screen.Maps)
        val route = setRoute(navController)
        val selectedItemBackgroundColor = LightGray
        val unselectedItemBackgroundColor = DarkGray
        BottomNavigation(
            modifier = Modifier.height(large)
                .fillMaxWidth()
                .clip(RoundedCornerShape(smallXXX)),
            backgroundColor = LightBlue,
        ) {
            screens.forEach { screen ->
                val isSelected = screen.route == route
                BottomNavigationItem(
                    icon = {
                        Icon(
                            modifier = Modifier.size(large),
                            painter = setPainterResource(screen),
                            contentDescription = "$route screen",
                        )
                    },
                    selected = isSelected,
                    onClick = { if (!isSelected) navController.navigate(screen.route) },
                    selectedContentColor = selectedItemBackgroundColor,
                    unselectedContentColor = unselectedItemBackgroundColor,
                )
            }
        }
    }
}

@Composable
private fun MainNavigationHost(navController: NavHostController) {
    val journeysViewModel = hiltViewModel<JourneysViewModel>()
    val journeysState = journeysViewModel.state.collectAsState().value
    val journeysEvent = journeysViewModel::handleEvent

    val stopsViewModel = hiltViewModel<StopsViewModel>()
    val stopsState = stopsViewModel.state.collectAsState().value
    val stopsEvent = stopsViewModel::handleEvent

    val tripsViewModel = hiltViewModel<TripsViewModel>()
    val tripsState = tripsViewModel.state.collectAsState().value
    val tripsEvent = tripsViewModel::handleEvent

    val mapsViewModel = hiltViewModel<MapsViewModel>()
    val mapsState = mapsViewModel.state.collectAsState().value
    val mapsEvent = mapsViewModel::handleEvent

    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsState = settingsViewModel.state.collectAsState().value
    val settingsEvent = settingsViewModel::handleEvent

    NavHost(navController, startDestination = Screen.Journeys.route) {
        composable(Screen.Journeys.route) {
            JourneysScreen(
                journeysState = journeysState,
                journeysEvent = journeysEvent,
                stopsState = stopsState,
                stopsEvent = stopsEvent,
                tripsState = tripsState,
                tripsEvent = tripsEvent,
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, settingsViewModel)
        }
        composable(Screen.Maps.route) {
            MapsScreen(
                mapsState = mapsState,
                mapsEvent = mapsEvent,
                journeysState = journeysState,
                journeysEvent = journeysEvent,
                stopsState = stopsState,
                stopsEvent = stopsEvent,
            )
        }
        //
        // Settings NavHost Screens
        composable(Screen.AppInfo.route) {
            AppInfoSettingsScreen(navController)
        }
        composable(Screen.Language.route) {
            LanguageSettingsScreen(navController, settingsState, settingsEvent)
        }
    }
}

@Composable
private fun setRoute(navController: NavController): String {
    return when (navController.currentBackStackEntryAsState().value?.destination?.route) {
        Screen.Journeys.route -> "Journeys"
        Screen.Settings.route -> "Settings"
        Screen.Maps.route -> "Maps"
        Screen.AppInfo.route -> "Settings"
        Screen.Language.route -> "Settings"
        Screen.DarkMode.route -> "Settings"
        Screen.InformationPrivacy.route -> "Settings"
        else -> { "" }
    }
}

@Composable
private fun setPainterResource(screen: Screen) = when (screen) {
    Screen.Journeys -> painterResource(id = R.drawable.icon_journeys)
    Screen.Settings -> painterResource(id = R.drawable.icon_berlin_go)
    Screen.Maps -> painterResource(id = R.drawable.icon_maps)
    // Settings Routes not part of BottomNav - so default 0 means no icons
    Screen.AppInfo -> painterResource(0)
    Screen.Language -> painterResource(0)
    Screen.DarkMode -> painterResource(0)
    Screen.InformationPrivacy -> painterResource(0)
}
