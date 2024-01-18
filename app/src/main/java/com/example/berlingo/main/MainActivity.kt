package com.example.berlingo.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.berlingo.R
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysViewModel
import com.example.berlingo.journeys.legs.stops.StopsViewModel
import com.example.berlingo.map.MapsScreen
import com.example.berlingo.map.MapsViewModel
import com.example.berlingo.routes.JourneysScreen
import com.example.berlingo.trips.TripsViewModel
import com.example.berlingo.ui.theme.BerlinGoTheme
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightBlue
import com.example.berlingo.ui.theme.LightGray
import dagger.hilt.android.AndroidEntryPoint

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MainActivity::class)

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        val screens = listOf(Screen.Journeys, Screen.Maps)
        val route = navController.currentBackStackEntryAsState().value?.destination?.route
        val selectedItemBackgroundColor = LightGray
        val unselectedItemBackgroundColor = DarkGray
        BottomNavigation(
            modifier = Modifier.height(64.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            backgroundColor = LightBlue,
        ) {
            screens.forEach { screen ->
                val isSelected = screen.route == route
                BottomNavigationItem(
                    icon = {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            painter = when (screen) {
                                Screen.Journeys -> painterResource(id = R.drawable.icon_journeys)
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
    object Journeys : Screen("journeys")
    object Maps : Screen("Maps")
}
