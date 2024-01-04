package com.example.berlingo.routes

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.berlingo.R
import com.example.berlingo.data.network.responses.Journeys
import com.example.berlingo.data.network.responses.Leg
import com.example.berlingo.data.network.responses.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesScreen(routesViewModel: RoutesViewModel) {
    Surface(color = Color.Gray, modifier = Modifier.fillMaxSize()) {
        // State for the two text fields

        var searchText by remember { mutableStateOf("") }
        val searchLocations = routesViewModel.searchLocations.collectAsState()
        val selectedLocation = routesViewModel.selectedLocation.collectAsState()
        var searchedJourneys by remember {
            mutableStateOf(
                listOf(
                    Journeys(
                        type = "",
                        listOf(Leg()),
                    ),
                ),
            )
        }
        var searchedLegs by remember { mutableStateOf(listOf(Leg())) }

        var searchedStopovers by remember { mutableStateOf(listOf(Trip.Stopover())) }
        var searchedTrips by remember { mutableStateOf(listOf(Trip())) }
        val state = routesViewModel.state.collectAsState()
        var textFieldOriginFocused by remember { mutableStateOf(false) }
        var textFieldDestinationFocused by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        var expandedItemIndex by remember { mutableStateOf(-1) }

        var stateOriginTextField by remember { mutableStateOf("") }
        var stateDestinationTextField by remember { mutableStateOf("") }
        var stateOriginNameId by remember { mutableStateOf("") }
        var stateDestinationNameId by remember { mutableStateOf("") }
        var stateOriginLongitude by remember { mutableStateOf("") }
        var stateOriginLatitude by remember { mutableStateOf("") }
        var stateDestinationLongitude by remember { mutableStateOf("") }
        var stateDestinationLatitude by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Top part with TextFields and Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Adjust the weight to control the space distribution
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    routesViewModel.queryJourneys(
                        "",
                        "",
                        0.0,
                        0.0,
                    )
                }
                TextField(
                    value = stateOriginTextField,
                    onValueChange = {
                        searchText = it
                        stateOriginTextField = it
                        runBlocking { routesViewModel.queryLocation(it) }
                    },
                    label = { Text("A") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .onFocusChanged { focusState ->
                            textFieldOriginFocused = focusState.isFocused
                        },
                )
                TextField(
                    value = stateDestinationTextField,
                    onValueChange = {
                        stateDestinationTextField = it
                        runBlocking { routesViewModel.queryLocation(it) }
                    },
                    label = { Text("B") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .onFocusChanged { focusState ->
                            textFieldDestinationFocused = focusState.isFocused
                        },
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Button(
                        onClick = {
                            Log.d(
                                "dev-log",
                                "$stateOriginTextField $stateDestinationTextField ${stateDestinationLatitude.toDouble()} $stateOriginNameId",
                            )
                            val fromId = routesViewModel.searchedJourneys.value
                            CoroutineScope(Dispatchers.IO).launch {
                                routesViewModel.queryJourneys(
                                    stateOriginNameId,
                                    stateDestinationNameId,
                                    stateDestinationLatitude.toDouble(),
                                    stateDestinationLongitude.toDouble(),
                                )
                                searchedJourneys = routesViewModel.searchedJourneys.value
                                searchedLegs = routesViewModel.searchedLegs.value
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterEnd),

                    ) {
                        // Icon on the left of the text
                        Icon(
                            painter = painterResource(id = R.drawable.icon_search),
                            contentDescription = null,
                        )
                    }
                }
            }
            if (searchLocations.value.isNotEmpty()) {
                LazyColumn {
                    items(searchLocations.value) { result ->
                        Text(
                            modifier = Modifier.clickable {
                                if (textFieldOriginFocused) {
                                    stateOriginTextField = result.name
                                    stateOriginNameId = result.id
                                    stateOriginLongitude = result.location.longitude
                                    stateOriginLatitude = result.location.latitude
                                    CoroutineScope(Dispatchers.IO).launch {
                                        routesViewModel.queryLocation(
                                            "",
                                        )
                                    }
                                }
                                if (textFieldDestinationFocused) {
                                    stateDestinationTextField = result.name
                                    stateDestinationNameId = result.id
                                    stateDestinationLongitude = result.location.longitude
                                    stateDestinationLatitude = result.location.latitude
                                    CoroutineScope(Dispatchers.IO).launch {
                                        routesViewModel.queryLocation(
                                            "",
                                        )
                                    }
                                }
                            },
                            text = result.name,
                            color = Color.Yellow,
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
            ) {
                itemsIndexed(searchedLegs) { index, leg ->
                    Text(
                        text = leg.departure ?: "",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                expandedItemIndex = if (index == expandedItemIndex) -1 else index
                                CoroutineScope(Dispatchers.IO).launch {
                                    routesViewModel.queryTripById(
                                        searchedLegs[expandedItemIndex].tripId ?: "",
                                    )
                                    searchedStopovers =
                                        routesViewModel.searchedStopovers.value ?: emptyList()
                                }
                            },
                    )
                    if (index == expandedItemIndex) {
                        Box(modifier = Modifier.heightIn(max = 200.dp)) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                items(searchedStopovers) { stopover ->
                                    Log.d("dev-tag", "STOP NAME ${stopover.stop?.name} ")

                                    Text(
                                        "${stopover.stop?.name}",
                                        modifier = Modifier.padding(8.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
