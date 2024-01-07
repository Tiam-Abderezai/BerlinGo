package com.example.berlingo.journeys.columns

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.berlingo.R
import com.example.berlingo.data.network.responses.Destination
import com.example.berlingo.data.network.responses.Journey
import com.example.berlingo.data.network.responses.Leg
import com.example.berlingo.data.network.responses.Origin
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.data.network.responses.Trip
import com.example.berlingo.journeys.JourneysViewEvent
import com.example.berlingo.journeys.JourneysViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StopsQuerySection(
    viewState: State<JourneysViewState>,
    onEvent: suspend (JourneysViewEvent) -> Unit,
) {
    var originLocation by remember { mutableStateOf(Stop().location) }
    var destinationLocation by remember { mutableStateOf(Stop().location) }
    var originStop by remember { mutableStateOf(Stop()) }
    var destinStop by remember { mutableStateOf(Stop()) }
    val stops = viewState.value.stops
    var textFieldOriginFocused by remember { mutableStateOf(false) }
    var textFieldDestinationFocused by remember { mutableStateOf(false) }

    var originStopName by remember { mutableStateOf("") }
    var destinStopName by remember { mutableStateOf("") }
    var originLocationName by remember { mutableStateOf("") }
    var destinLocationName by remember { mutableStateOf("") }
    var originLocationId by remember { mutableStateOf(0) }
    var destinLocationId by remember { mutableStateOf(0) }
    var originLocationLat by remember { mutableStateOf("") }
    var destinLocationLat by remember { mutableStateOf("") }
    var originLocationLong by remember { mutableStateOf("") }
    var destinLocationLong by remember { mutableStateOf("") }

    var queryText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        TextField(
            value = originStopName,
            onValueChange = { query ->
                Log.d("dev-log", "query origin: $query")
                originStopName = query
                CoroutineScope(Dispatchers.IO).launch {
                    onEvent.invoke(
                        JourneysViewEvent.StopsQueryEvent(
                            query,
                        ),
                    )
                }
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
            value = destinStopName,
            onValueChange = { query ->
                destinStopName = query
                CoroutineScope(Dispatchers.IO).launch {
                    onEvent.invoke(
                        JourneysViewEvent.StopsQueryEvent(query),
                    )
                }
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
                    CoroutineScope(Dispatchers.IO).launch {
                        onEvent.invoke(
                            JourneysViewEvent.JourneyQueryEvent(
                                from = originLocationId.toString(),
                                to = originLocationId.toString(),
                                toLatitude = destinLocationLat.toDouble(),
                                toLongitude = destinLocationLong.toDouble(),
                            ),
                        )
                        // ------------------------------ //
                        // Hardcoded Values for Debugging //
//                        onEvent.invoke(
//                            JourneysViewEvent.JourneyQueryEvent(
//                                "900064301",
//                                "900003200",
//                                52.525607,
//                                13.369072,
//                            ),
//                        )
//                        searchedJourneys = viewState.value.journeys
                        // ------------------------------ //
                        // ------------------------------ //
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterEnd),

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = null,
                )
            }
        }
    }
    if (viewState.value.stops?.isNotEmpty() == true) {
        LazyColumn {
            items(stops ?: emptyList()) { stop ->
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
                            onEvent.invoke(
                                JourneysViewEvent.StopsQueryEvent(
                                    "",
                                ),
                            )
                        }
                    },
                    text = stop.name ?: "",
                    color = Color.Yellow,
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LegsColumnPreview() {
    val viewState = remember { mutableStateOf(JourneysViewState()) }

    val leg1 = Leg(destination = Destination(name = "Hauptbahnhof"), departure = "11:11")
    val leg2 =
        Leg(Origin(name = "Lichterfelde"), Destination(name = "Hauptbahnhof"), departure = "11:11")
    val leg3 =
        Leg(Origin(name = "Lichterfelde"), Destination(name = "Hauptbahnhof"), departure = "11:11")
    val legs = listOf(leg1, leg2, leg3)

    val journey1 = Journey(legs = legs)
    val journey2 = Journey(legs = legs)
    val journey3 = Journey(legs = legs)

    val trip1 = Trip()

    val journeysPair = Pair(journey1, legs)
    val journeyMap = mapOf(journeysPair)

    val legsPair = Pair(leg1, "")
    val legsMap = mapOf(legsPair)

    StopsQuerySection(
        viewState = viewState,
        onEvent = {},
    )
}
