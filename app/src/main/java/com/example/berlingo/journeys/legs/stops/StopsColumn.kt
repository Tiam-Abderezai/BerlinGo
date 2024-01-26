package com.example.berlingo.journeys.legs.stops

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.berlingo.R
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.legs.stops.StopsState.*
import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
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
@SuppressLint("CoroutineCreationDuringComposition")
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
        TextField(
            label = { Text(text = "A", color = labelColor, fontWeight = FontWeight.SemiBold) },
            modifier = Modifier
                .background(color = backgroundColor)
                .fillMaxWidth()
                .padding(4.dp)
                .onFocusChanged { focusState ->
                    textFieldOriginFocused = focusState.isFocused
                },
            value = originStopName,
            onValueChange = { query ->
                originStopName = query
                CoroutineScope(Dispatchers.IO).launch {
                    stopsEvent.invoke(
                        StopsEvent.StopsQueryEvent(
                            query,
                        ),
                    )
                }
            },
        )
        TextField(
            modifier = Modifier
                .background(color = backgroundColor)
                .fillMaxWidth()
                .padding(4.dp)
                .onFocusChanged { focusState ->
                    textFieldDestinationFocused = focusState.isFocused
                },
            value = destinStopName,
            onValueChange = { query ->
                destinStopName = query
                CoroutineScope(Dispatchers.IO).launch {
                    stopsEvent.invoke(
                        StopsEvent.StopsQueryEvent(query),
                    )
                }
            },
            label = { Text("B", color = labelColor, fontWeight = FontWeight.SemiBold) },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
//                        if (destinLocationLat.isNotEmpty()) {
//                        logger.debug("originLocationId: $originLocationId")
//                        logger.debug("destinLocationId: $destinLocationId")
//                        logger.debug("destinLocationLat: $originLocationId")
//                        logger.debug("originLocationId: $destinLocationLong")
                        journeysEvent.invoke(
                            JourneysEvent.JourneyQueryEvent(
                                from = originLocationId.toString(),
                                to = destinLocationId.toString(),
                                toLatitude = destinLocationLat.toDouble(),
                                toLongitude = destinLocationLong.toDouble(),
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(id = R.drawable.icon_search),
                    tint = labelColor,
                    contentDescription = null,
                )
            }
        }
    }
    when (stopsState) {
        is Initial -> {}
        is Loading -> LoadingScreen()
        is Error -> ErrorScreen(message = stopsState.message)
        is Success -> DisplayStops(stopsState.data, stopsEvent)
    }
}

@Composable
fun DisplayStops(stopsState: List<Stop>, stopsEvent: suspend (StopsEvent) -> Unit) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    LazyColumn {
        items(stopsState ?: emptyList()) { stop ->
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
                    // TODO CLEAN THIS WORKAROUND
                    // Currently used to clear
                    // the list of Locations/Stops after
                    // focusing on either A or B TextField
                    CoroutineScope(Dispatchers.IO).launch {
                        stopsEvent.invoke(
                            StopsEvent.StopsQueryEvent(
                                "",
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
