package com.example.berlingo.map.columns

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.extensions.getDepartureTime
import com.example.berlingo.common.extensions.getLineNameIcon
import com.example.berlingo.common.extensions.getLineProductColor
import com.example.berlingo.common.extensions.getLineProductIcon
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.data.network.journeys.responses.Destination
import com.example.berlingo.data.network.journeys.responses.Journey
import com.example.berlingo.data.network.journeys.responses.Leg
import com.example.berlingo.data.network.journeys.responses.Origin
import com.example.berlingo.data.network.journeys.responses.Trip
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.map.MapsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("MapsJourneysColumn()")

@Composable
fun MapsJourneysColumn(
    viewState: State<JourneysState>,
    onMapsEvent: suspend (MapsEvent) -> Unit,
) {
    val journeys = viewState.value.journeys
    Box(modifier = Modifier.heightIn(100.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.2f),
        ) {
            itemsIndexed(journeys?.keys?.toList() ?: emptyList()) { indexJourney, journey ->
                val legs = journey.legs

                logger.debug("Journey: ${journeys?.size}")
                logger.debug("Legs: ${journey.legs}")
                logger.debug("TripIds: ${journey.legs?.get(0)?.tripId}")
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Black,
                    thickness = 0.5.dp,
                )
                Text(
//                    modifier = Modifier
//                        .padding(16.dp),
                    text = "${journey.legs?.get(0)?.departure?.getDepartureTime()}",
                )
                DrawLegsLineWithIcons(journey, legs, viewState, onMapsEvent, indexJourney)
            }
        }
    }
}

@Composable
fun DrawLegsLineWithIcons(
    journey: Journey?,
    legs: List<Leg>?,
    viewState: State<JourneysState>,
    onMapsEvent: suspend (MapsEvent) -> Unit,
    indexJourney: Int,
) {
    Row(
        modifier = Modifier
            .heightIn(50.dp)
            .clickable {
                CoroutineScope(Dispatchers.IO).launch {
                    onMapsEvent.invoke(
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun JourneysColumnPreview() {
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

    MapsJourneysColumn(
        viewState = viewState,
        onMapsEvent = {},
    )
}
