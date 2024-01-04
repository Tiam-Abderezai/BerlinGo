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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.berlingo.R
import com.example.berlingo.map.MapScreen
import com.example.berlingo.routes.RoutesScreen
import com.example.berlingo.routes.RoutesViewModel
import com.example.berlingo.ui.theme.BerlinGoTheme
import dagger.hilt.android.AndroidEntryPoint

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
    val routesViewModel = hiltViewModel<RoutesViewModel>()

//    viewModel
    NavHost(navController, startDestination = "routes") {
        composable("routes") { RoutesScreen(routesViewModel) }
        composable("map") { MapScreen() }
    }
}

fun currentRoute(navController: NavController): String? {
    return navController.currentBackStackEntry?.destination?.route
}
