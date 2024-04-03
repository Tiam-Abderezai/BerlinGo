package com.example.berlingo.routes

import JourneysColumn
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.legs.stops.StopsColumn
import com.example.berlingo.journeys.legs.stops.StopsEvent
import com.example.berlingo.journeys.legs.stops.StopsState
import com.example.berlingo.journeys.legs.stops.network.responses.Location
import com.example.berlingo.journeys.network.responses.Destination
import com.example.berlingo.journeys.network.responses.Journey
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.journeys.network.responses.Line
import com.example.berlingo.journeys.network.responses.Origin
import com.example.berlingo.trips.TripsEvent
import com.example.berlingo.trips.TripsState
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import com.example.berlingo.ui.theme.isDarkMode

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("JourneysScreen()")
private val testTag = "JourneysScreen()"

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun JourneysScreen(
    journeysState: JourneysState = JourneysState.Initial,
    journeysEvent: suspend (JourneysEvent) -> Unit = {},
    stopsState: StopsState = StopsState.Initial,
    stopsEvent: suspend (StopsEvent) -> Unit = {},
    tripsState: TripsState = TripsState.Initial,
    tripsEvent: suspend (TripsEvent) -> Unit,
) {
    val backgroundColor = if (isDarkMode()) DarkGray else LightGray
    Surface(
        color = backgroundColor,
        modifier = Modifier
            .testTag("$testTag: Surface()")
            .semantics {
                testTagsAsResourceId = true
            }
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .testTag("$testTag: Surface(): Column()")
                .fillMaxSize(),
        ) {
            StopsColumn(
                journeysEvent,
                stopsState,
                stopsEvent,
            )
            JourneysColumn(
                journeysState,
                journeysEvent,
                tripsState,
                tripsEvent,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun JourneysScreenPreview() {
    val leg1 = Leg(
        origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Destination(
            name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)",
            location = Location()
        ),
        plannedDeparture = "2024-04-03T01:26:00+02:00",
        departure = "11:11",
        line = Line(name = "M85", product = "bus")
    )
    val leg2 = Leg(
        origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Destination(
            name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)",
            location = Location()
        ),
        plannedDeparture = "2024-04-03T01:26:00+02:00",
        departure = "11:11",
        line = Line(name = "M48", product = "bus")
    )
    val leg3 = Leg(
        origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Destination(
            name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)",
            location = Location()
        ),
        plannedDeparture = "2024-04-03T01:26:00+02:00",
        departure = "11:11",
        line = Line(name = "RE8", product = "regional")
    )
    val leg4 = Leg(
        origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Destination(
            name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)",
            location = Location()
        ),
        plannedDeparture = "2024-04-03T01:26:00+02:00",
        departure = "11:11",
        line = Line(name = "S8", product = "suburban")
    )
    val legs = listOf(leg1, leg2, leg3, leg4)

    val journey1 = Journey(type = "journey", legs = legs)
    val journey2 = Journey(type = "journey", legs = legs)

    val journeys = mapOf(journey1 to legs, journey2 to legs)
    JourneysScreen(journeysState = JourneysState.Success(journeys)) {}

}
