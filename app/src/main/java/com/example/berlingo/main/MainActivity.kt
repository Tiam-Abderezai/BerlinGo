package com.example.berlingo.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.berlingo.R
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.map.MapsScreen
import com.example.berlingo.map.MapsViewModel
import com.example.berlingo.routes.RoutesScreen
import com.example.berlingo.routes.RoutesViewModel
import com.example.berlingo.journeys.JourneysViewModel
import com.example.berlingo.map.MapScreen
import com.example.berlingo.routes.JourneysScreen
import com.example.berlingo.ui.theme.BerlinGoTheme
import dagger.hilt.android.AndroidEntryPoint

private val logger: BaseLogger = FactoryLogger.getLoggerKClass(MainActivity::class)

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BerlinGoTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) },
                ) {
                    NavigationHost(navController = navController)
                }
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
//                ) {
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Color.Blue,
        contentColor = Color.White,
    ) {
        val currentRoute = currentRoute(navController)
//      Tab: Routes
        BottomNavigationItem(
            selected = currentRoute == "routes",
            selectedContentColor = Color.Green,
            onClick = {
                navController.navigate("routes") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.icon_routes),
                    contentDescription = null,
                )
            },
        )
//      Tab: Map
        BottomNavigationItem(
            selected = currentRoute == "map",
            selectedContentColor = Color.Green,
            onClick = {
                navController.navigate("map") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.icon_map),
                    contentDescription = null,
                )
            },
        )
//        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController) {
    val journeysViewModel = hiltViewModel<JourneysViewModel>()
    val journeysViewState = journeysViewModel.state.collectAsState()

    val mapsViewModel = hiltViewModel<MapsViewModel>()
    val mapsState = mapsViewModel.state.collectAsState()
//    viewModel

    NavHost(navController, startDestination = "routes") {
        composable("routes") { JourneysScreen(viewState = journeysViewState, onEvent = journeysViewModel::handleEvent) }
        composable("map") { MapsScreen(mapsState, mapsViewModel::handleEvent) }
    }
}

fun currentRoute(navController: NavController): String? {
    return navController.currentBackStackEntry?.destination?.route
}
