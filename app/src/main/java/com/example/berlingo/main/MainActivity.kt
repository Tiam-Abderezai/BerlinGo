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
                    NavigationHost(navController = navController)
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
        val route = navController.currentBackStackEntryAsState().value?.destination?.route
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
                            painter = when (screen) {
                                Screen.Journeys -> painterResource(id = R.drawable.icon_journeys)
                                Screen.Settings -> painterResource(id = R.drawable.icon_berlin_go)
                                Screen.Maps -> painterResource(id = R.drawable.icon_maps)
                            },
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
fun NavigationHost(navController: NavHostController) {
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
            SettingsScreen()
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
    }
}

sealed class Screen(val route: String) {
    object Journeys : Screen("Journeys")
    object Settings : Screen("Settings")
    object Maps : Screen("Maps")
}
