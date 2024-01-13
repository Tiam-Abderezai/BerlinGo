package com.example.berlingo.routes

import JourneysColumn
import LegsColumn
import StopoversColumn
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.journeys.responses.Destination
import com.example.berlingo.data.network.journeys.responses.Journey
import com.example.berlingo.data.network.journeys.responses.Leg
import com.example.berlingo.data.network.journeys.responses.Origin
import com.example.berlingo.data.network.journeys.responses.Trip
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.columns.StopsQuerySection

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("JourneysScreen()")

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneysScreen(
    viewState: State<JourneysState>,
    onEvent: suspend (JourneysEvent) -> Unit,
) {
//    logger.debug("")

    Surface(color = Color.Gray, modifier = Modifier.fillMaxSize()) {
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
    val viewState = remember { mutableStateOf(JourneysState()) }
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
        journey = journey1,
    )
    StopoversColumn(
        viewState = viewState,
        onEvent = {},
    )
}
