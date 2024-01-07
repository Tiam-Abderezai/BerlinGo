package com.example.berlingo.routes

import JourneysColumn
import LegsColumn
import StopoversColumn
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.berlingo.data.network.responses.Destination
import com.example.berlingo.data.network.responses.Journey
import com.example.berlingo.data.network.responses.Leg
import com.example.berlingo.data.network.responses.Origin
import com.example.berlingo.data.network.responses.Stop
import com.example.berlingo.data.network.responses.Trip
import com.example.berlingo.journeys.JourneysViewEvent
import com.example.berlingo.journeys.JourneysViewState
import com.example.berlingo.journeys.columns.StopsQuerySection

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneysScreen(
    viewState: State<JourneysViewState>,
    onEvent: suspend (JourneysViewEvent) -> Unit,
) {
    Surface(color = Color.Gray, modifier = Modifier.fillMaxSize()) {
//        val locationState = state.value.location
//        val selectedLocation = journeysViewModel.selectedLocation.collectAsState()
        var stateLocations by remember {
            mutableStateOf<List<Stop>>(emptyList())
        }
        var searchedJourneys by remember {
            mutableStateOf<Map<Journey, List<Leg>>>(emptyMap())
        }
        var searchedLegs by remember {
            mutableStateOf<Map<Leg, String>>(emptyMap())
        }

        var searchedTrips by remember {
            mutableStateOf(Trip())
        }
        var originStopIndexed by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        var expandedJourneyItemIndex by remember { mutableStateOf(-1) }
        var expandedLegItemIndex by remember { mutableStateOf(-1) }

        var stateOriginNameId by remember { mutableStateOf("") }
        var stateDestinationNameId by remember { mutableStateOf("") }
        var stateOriginLongitude by remember { mutableStateOf("") }
        var stateOriginLatitude by remember { mutableStateOf("") }
        var stateDestinationLongitude by remember { mutableStateOf("") }
        var stateDestinationLatitude by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            StopsQuerySection(
                viewState,
                onEvent,
            )

            JourneysColumn(
                viewState,
                onEvent,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun JourneysScreenPreview() {
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
    JourneysScreen(viewState) {}
    JourneysColumn(
        viewState = viewState,
        onEvent = {},
    )
    LegsColumn(
        viewState = viewState,
        onEvent = {},
    )
    StopoversColumn(
        viewState = viewState,
        onEvent = {},
    )
}
