package com.example.berlingo.map.columns

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions
import com.example.berlingo.common.Dimensions.medium
import com.example.berlingo.common.Dimensions.small
import com.example.berlingo.common.Dimensions.smallXXX
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.extensions.convertEpochTime
import com.example.berlingo.common.extensions.getLineNameIcon
import com.example.berlingo.common.extensions.getLineProductColor
import com.example.berlingo.common.extensions.getLineProductIcon
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
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
        is JourneysState.Initial -> EmptyMapsJourneys()
        is JourneysState.Loading -> LoadingScreen()
        is JourneysState.Error -> ErrorScreen(message = journeysState.message)
        is JourneysState.Success -> MapsJourneys(journeysState.journeys, mapsEvent)
    }
}

@Composable
private fun MapsJourneys(journeys: Map<Journey, List<Leg>>, mapsEvent: suspend (MapsEvent) -> Unit) {
    Box(modifier = Modifier.height(Dimensions.mapBoxHeight)) {
        val textColor = if (isSystemInDarkTheme()) LightGray else DarkGray
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            itemsIndexed(journeys.keys.toList()) { indexJourney, journey ->
                val legs = journey.legs
                val plannedDeparture =
                    journey.legs?.get(0)?.plannedDeparture?.convertEpochTime().toString()
                val departure = journey.legs?.get(0)?.departure?.convertEpochTime().toString()
                val departureDelay = journey.legs?.get(0)?.departureDelay
                val cancelled = journey.legs?.get(0)?.cancelled

                logger.debug("Journey: ${journeys.size}")
                logger.debug("Legs: ${journey.legs}")
                logger.debug("TripIds: ${journey.legs?.get(0)?.tripId}")
                Row() {
                    if (cancelled == false) {
                        Column() {
                            Text(
                                text = plannedDeparture,
                                color = textColor,
                            )
                            if (departureDelay != 0 && departureDelay != null) {
                                Text(
                                    text = departure,
                                    color = Color.Red,
                                )
                                Text(
                                    text = "($departureDelay)",
                                    color = Color.Red,
                                )
                            }
                        }
                    } else {
                        Text(
                            text = plannedDeparture,
                            color = Color.Red,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        )
                    }
                    DrawLegsLineWithIcons(journey, legs, mapsEvent)
                }
                Divider()
            }
        }
    }
}

@Composable
private fun EmptyMapsJourneys() {
    Box(modifier = Modifier.height(Dimensions.mapBoxHeight))
}

@Composable
fun DrawLegsLineWithIcons(
    journey: Journey?,
    legs: List<Leg>?,
    mapsEvent: suspend (MapsEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .heightIn(small)
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
            val line = leg.line
            val product = line?.product
            val walking = leg.walking
            val lineProductIcon =
                if (walking == true) R.drawable.icon_change_station else product?.getLineProductIcon() ?: 0
            val lineProductColor = if (walking == true) Color.Black else product?.getLineProductColor() ?: Color.Transparent
            val lineNameIcon = if (walking == true) R.drawable.icon_walking else line?.name?.getLineNameIcon() ?: 0
            DrawLineProductImage(lineProductIcon, lineNameIcon)
            logger.debug("line?.name: ${leg.line?.name}")
            logger.debug("lineProductIcon: $lineProductIcon")
            logger.debug("lineNameIcon: $lineNameIcon")
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(smallXXX),
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
            modifier = Modifier.size(medium),
            painter = painterResource(id = lineProductIcon),
            contentDescription = null,
        )
        Image(
            modifier = Modifier.size(medium),
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
