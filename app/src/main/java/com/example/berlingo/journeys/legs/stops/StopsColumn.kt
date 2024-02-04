package com.example.berlingo.journeys.legs.stops

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions.large
import com.example.berlingo.common.Dimensions.smallXX
import com.example.berlingo.common.Dimensions.smallXXX
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.legs.stops.StopsState.*
import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import com.example.berlingo.main.MainActivity
import com.example.berlingo.main.locationPermissionGranted
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("StopsColumn()")
var textFieldOriginFocused by mutableStateOf(false)
var textFieldDestinationFocused by mutableStateOf(false)

var originStopName by mutableStateOf("")
var destinStopName by mutableStateOf("")

var queryText by mutableStateOf("")
var originLocationName by mutableStateOf("")
var destinLocationName by mutableStateOf("")
var originLocationId by mutableStateOf(0)
var destinLocationId by mutableStateOf(0)
var originLocationLat by mutableStateOf("")
var destinLocationLat by mutableStateOf("")
var originLocationLong by mutableStateOf("")
var destinLocationLong by mutableStateOf("")
var originLocation by mutableStateOf(Stop().location)
var destinationLocation by mutableStateOf(Stop().location)
var originStop by mutableStateOf(Stop())
var destinStop by mutableStateOf(Stop())
var displayStops by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "MissingPermission")
@Composable
fun StopsColumn(
    journeyState: JourneysState,
    journeysEvent: suspend (JourneysEvent) -> Unit,
    stopsState: StopsState,
    stopsEvent: suspend (StopsEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        val backgroundColor = if (isSystemInDarkTheme()) DarkGray else LightGray
        val labelColor = if (isSystemInDarkTheme()) LightGray else DarkGray
        val context = LocalContext.current
        val fusedLocationProviderClient =
            remember { LocationServices.getFusedLocationProviderClient(context) }
        TextField(
            label = { Text(text = "A", color = labelColor, fontWeight = FontWeight.SemiBold) },
            modifier = Modifier
                .background(color = backgroundColor)
                .fillMaxWidth()
                .padding(smallXX)
                .onFocusChanged { focusState ->
                    textFieldOriginFocused = focusState.isFocused
                },
            value = originStopName,
            onValueChange = { query ->
                originStopName = query
                CoroutineScope(Dispatchers.IO).launch {
                    stopsEvent.invoke(
                        StopsEvent.GetStops(
                            query,
                        ),
                    )
                }
            },
            trailingIcon = {
                Row {
                    if (originStopName.isNotEmpty()) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = stringResource(R.string.clear_textfield),
                            modifier = Modifier.clickable {
                                originStopName = ""
                                CoroutineScope(Dispatchers.IO).launch { stopsEvent.invoke(StopsEvent.EmptyStops()) }
                            },
                        )
                    }
                    Spacer(modifier = Modifier.width(smallXX))
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = stringResource(R.string.get_current_location),
                        modifier = Modifier.clickable {
                            if (locationPermissionGranted.value) {
                                val taskLocation = fusedLocationProviderClient.lastLocation
                                taskLocation.addOnCompleteListener(context as MainActivity) { task ->
                                    if (task.isComplete) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            stopsEvent.invoke(
                                                StopsEvent.GetNearestStops(
                                                    task.result,
                                                ),
                                            )
                                        }
                                    }
                                }
                            }
                        },
                    )
                    Spacer(modifier = Modifier.width(smallXX))
                }
            },
        )
        TextField(
            label = { Text("B", color = labelColor, fontWeight = FontWeight.SemiBold) },
            modifier = Modifier
                .background(color = backgroundColor)
                .fillMaxWidth()
                .padding(smallXX)
                .onFocusChanged { focusState ->
                    textFieldDestinationFocused = focusState.isFocused
                },
            value = destinStopName,
            onValueChange = { query ->
                destinStopName = query
                CoroutineScope(Dispatchers.IO).launch {
                    stopsEvent.invoke(
                        StopsEvent.GetStops(query),
                    )
                }
            },
            trailingIcon = {
                if (destinStopName.isNotEmpty()) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.clear_textfield),
                        modifier = Modifier.clickable { destinStopName = "" },
                    )
                }
            },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(smallXX),
        ) {
            Button(
                onClick = {
                    if (originStopName.isNotBlank() && destinStopName.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            journeysEvent.invoke(
                                JourneysEvent.JourneyQueryEvent(
                                    from = originLocationId.toString(),
                                    to = destinLocationId.toString(),
                                    toLatitude = destinLocationLat,
                                    toLongitude = destinLocationLong,
                                ),
                            )
                            // ------------------------------ //
                            // Hardcoded Values for Debugging //
//                        }
//                        journeysEvent.invoke(
//                            JourneysEvent.JourneyQueryEvent(
//                                "900064301",
//                                "900003200",
//                                52.525607,
//                                13.369072,
//                            ),
//                        )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(large)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(smallXXX),
            ) {
                Icon(
                    modifier = Modifier.size(large),
                    painter = painterResource(id = R.drawable.icon_search),
                    tint = labelColor,
                    contentDescription = null,
                )
            }
        }
    }
    HandleStopsState(stopsState, stopsEvent)
}

@Composable
private fun HandleStopsState(
    stopsState: StopsState,
    stopsEvent: suspend (StopsEvent) -> Unit,
) {
    when (stopsState) {
        is Initial -> {}
        is Loading -> LoadingScreen()
        is Error -> ErrorScreen(message = stopsState.message)
        is Success -> {
            DisplayStops(stopsState.stops, stopsEvent)
            logger.debug("SHOW ME THIS SHIT $originStopName")
            if (stopsState.nearestStop.name?.isNotEmpty() == true) {
                // When user clicks on the current user location icon, it
                // fills the origin text field "A" with user location name
                fillOriginTextField(stopsState.nearestStop)
            }
        }
    }
}

private fun fillOriginTextField(nearestStop: Stop) {
    originStopName = nearestStop.name ?: ""
    originLocationId = nearestStop.id?.toInt() ?: 0
    originLocationLat = nearestStop.location?.latitude ?: ""
    originLocationLong = nearestStop.location?.longitude ?: ""
    originStop = nearestStop
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisplayStops(stopsState: List<Stop>, stopsEvent: suspend (StopsEvent) -> Unit) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    LazyColumn {
        items(stopsState) { stop ->
            val keyboardController = LocalSoftwareKeyboardController.current
            Text(
                modifier = Modifier.clickable {
                    if (textFieldOriginFocused) {
                        originStopName = stop.name ?: ""
                        originLocationId = stop.location?.id ?: 0
                        originLocationLat = stop.location?.latitude ?: ""
                        originLocationLong = stop.location?.longitude ?: ""
                        originStop = stop
                    }
                    if (textFieldDestinationFocused) {
                        destinStopName = stop.name ?: ""
                        destinLocationId = stop.location?.id ?: 0
                        destinLocationLat = stop.location?.latitude ?: ""
                        destinLocationLong = stop.location?.longitude ?: ""
                        destinStop = stop
                    }
                    keyboardController?.hide()
                    // Used to clear the list of Locations/Stops
                    // after focusing on either A or B TextField
                    CoroutineScope(Dispatchers.IO).launch {
                        stopsEvent.invoke(
                            StopsEvent.EmptyStops(
                                emptyList(),
                            ),
                        )
                    }
                },
                text = stop.name ?: "",
                color = textColor,
            )
        }
    }
}

// @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
// @Composable
// fun LegsColumnPreview() {
//    val viewState = remember { mutableStateOf(JourneysState()) }
//
//    val leg1 = Leg(destination = Destination(name = "Hauptbahnhof"), departure = "11:11")
//    val leg2 =
//        Leg(Origin(name = "Lichterfelde"), Destination(name = "Hauptbahnhof"), departure = "11:11")
//    val leg3 =
//        Leg(Origin(name = "Lichterfelde"), Destination(name = "Hauptbahnhof"), departure = "11:11")
//    val legs = listOf(leg1, leg2, leg3)
//
//    val journey1 = Journey(legs = legs)
//    val journey2 = Journey(legs = legs)
//    val journey3 = Journey(legs = legs)
//
//    val trip1 = Trip()
//
//    val journeysPair = Pair(journey1, legs)
//    val journeyMap = mapOf(journeysPair)
//
//    val legsPair = Pair(leg1, "")
//    val legsMap = mapOf(legsPair)
//
//    StopsQuerySection(
//        viewState = viewState,
//        onEvent = {},
//    )
// }
