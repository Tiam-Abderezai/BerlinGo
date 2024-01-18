package com.example.berlingo.map.columns

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.extensions.getDepartureTime
import com.example.berlingo.common.extensions.getLineNameIcon
import com.example.berlingo.common.extensions.getLineProductColor
import com.example.berlingo.common.extensions.getLineProductIcon
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.utils.ErrorScreen
import com.example.berlingo.common.utils.LoadingScreen
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.network.responses.Journey
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.map.MapsEvent
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("MapsJourneysColumn()")

@Composable
fun MapsJourneysColumn(
    journeysState: JourneysState,
    mapsEvent: suspend (MapsEvent) -> Unit,
) {
    when (journeysState) {
        is JourneysState.Initial -> {}
        is JourneysState.Loading -> LoadingScreen()
        is JourneysState.Error -> ErrorScreen(message = journeysState.message)
        is JourneysState.Success -> DisplayMapJourneys(journeysState.data, mapsEvent)
    }
}

@Composable
private fun DisplayMapJourneys(journeys: Map<Journey, List<Leg>>, mapsEvent: suspend (MapsEvent) -> Unit) {
    Box(modifier = Modifier.height(228.dp)) {
        val textColor = if (isSystemInDarkTheme()) LightGray else DarkGray
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(1f),
        ) {
            itemsIndexed(journeys.keys.toList()) { indexJourney, journey ->
                val legs = journey.legs
                logger.debug("Journey: ${journeys.size}")
                logger.debug("Legs: ${journey.legs}")
                logger.debug("TripIds: ${journey.legs?.get(0)?.tripId}")
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Black,
                    thickness = 0.8.dp,
                )
                Row() {
                    Text(
                        text = "${journey.legs?.get(0)?.departure?.getDepartureTime()}",
                        color = textColor,
                    )
                    DrawLegsLineWithIcons(journey, legs, mapsEvent)
                }
            }
        }
    }
}

@Composable
fun DrawLegsLineWithIcons(
    journey: Journey?,
    legs: List<Leg>?,
    mapsEvent: suspend (MapsEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .heightIn(1.dp)
            .clickable {
                CoroutineScope(Dispatchers.IO).launch {
                    mapsEvent.invoke(
                        MapsEvent.DirectionsJourneyGet(
                            journey ?: Journey(),
                        ),
                    )
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        legs?.forEach { leg ->
            val lineProductIcon = leg.line?.product?.getLineProductIcon() ?: 0
            val lineProductColor = leg.line?.product?.getLineProductColor() ?: Color.Transparent
            val lineNameIcon = leg.line?.name?.getLineNameIcon() ?: 0
            DrawLineProductImage(lineProductIcon, lineNameIcon)
            logger.debug("line?.name: ${leg.line?.name}")
            logger.debug("lineProductIcon: $lineProductIcon")
            logger.debug("lineNameIcon: $lineNameIcon")
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
            ) {
                drawLine(
                    color = lineProductColor,
                    start = Offset(0f, center.y),
                    end = Offset(size.width, center.y),
                    strokeWidth = Stroke.DefaultMiter,
                )
            }
        }
    }
}

@Composable
private fun DrawLineProductImage(lineProductIcon: Int, lineNameIcon: Int) {
    // 0 means ignore don't display Icon if no product is found
    if (lineProductIcon != 0 && lineNameIcon != 0) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = lineProductIcon),
            contentDescription = null,
        )
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = lineNameIcon),
            contentDescription = null,
        )
    }
}

// @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
// @Composable
// fun JourneysColumnPreview() {
//    val viewState = remember { mutableStateOf(JourneysState()) }
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
//    MapsJourneysColumn(
//        viewState = viewState,
//        onMapsEvent = {},
//    )
// }
