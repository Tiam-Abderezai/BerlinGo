package com.example.berlingo.routes

import JourneysColumn
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.legs.stops.StopsColumn
import com.example.berlingo.journeys.legs.stops.StopsEvent
import com.example.berlingo.journeys.legs.stops.StopsState

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("JourneysScreen()")

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneysScreen(
    journeysState: JourneysState,
    journeysEvent: suspend (JourneysEvent) -> Unit,
    stopsState: StopsState,
    stopsEvent: suspend (StopsEvent) -> Unit,
) {
    logger.debug("originLocationId:")
//    val stopsViewModel = hiltViewModel<StopsViewModel>()
//    val stopsState = stopsViewModel.state.collectAsState().value
//    val stopsEvent = stopsViewModel::handleEvent
    Surface(color = Color.Gray, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            StopsColumn(
                journeysState,
                journeysEvent,
                stopsState,
                stopsEvent,
            )
            JourneysColumn(
                journeysState,
                journeysEvent,
            )
        }
    }
}
// @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
// @Composable
// fun JourneysScreenPreview() {
//    val viewState = remember { mutableStateOf(JourneysState()) }
//    val leg1 = Leg(destination = Destination(name = "Hauptbahnhof"), departure = "11:11")
//    val leg2 = Leg(Origin(name = "Lichterfelde"), Destination(name = "Hauptbahnhof"), departure = "11:11")
//    val leg3 = Leg(Origin(name = "Lichterfelde"), Destination(name = "Hauptbahnhof"), departure = "11:11")
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
//    JourneysScreen(viewState) {}
//    JourneysColumn(
//        viewState = viewState,
//    ) {}
//    LegsColumn(
//        viewState = viewState,
//        onEvent = {},
//        legs = legs,
//    )
// //    StopoversColumn(
// //        viewState = viewState,
// //        onEvent = {},
// //    )
// }
