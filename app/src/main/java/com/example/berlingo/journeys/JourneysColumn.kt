import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.berlingo.R
import com.example.berlingo.common.components.Divider
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.extensions.calculateDelay
import com.example.berlingo.common.extensions.convertEpochDate
import com.example.berlingo.common.extensions.convertEpochTime
import com.example.berlingo.common.extensions.getLineNameIcon
import com.example.berlingo.common.extensions.getLineProductColor
import com.example.berlingo.common.extensions.getLineProductIcon
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.JourneysState.Error
import com.example.berlingo.journeys.JourneysState.Initial
import com.example.berlingo.journeys.JourneysState.Loading
import com.example.berlingo.journeys.JourneysState.Success
import com.example.berlingo.journeys.network.responses.Journey
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.journeys.network.responses.Remark
import com.example.berlingo.trips.TripsEvent
import com.example.berlingo.trips.TripsState
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("JourneysColumn()")

@Composable
fun JourneysColumn(
    journeysState: JourneysState,
    journeyEvent: suspend (JourneysEvent) -> Unit,
    tripsState: TripsState,
    tripsEvent: suspend (TripsEvent) -> Unit,
) {
    when (journeysState) {
        is Initial -> {}
        is Loading -> LoadingScreen()
        is Error -> ErrorScreen(message = journeysState.message)
        is Success -> {
            Box() {
                Column() {
                    if (journeysState.warningRemark?.type == "warning") {
                        DisplayWarningRemark(journeysState.warningRemark)
                    }
                    DisplayJourneys(journeysState.journeys, journeyEvent, tripsState, tripsEvent)
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun DisplayJourneys(
    journeys: Map<Journey, List<Leg>>,
    journeyEvent: suspend (JourneysEvent) -> Unit,
    tripsState: TripsState,
    tripsEvent: suspend (TripsEvent) -> Unit,
) {
    Divider()
    Box() {
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
                val departureDelay = journey.legs?.get(0)?.departureDelay?.calculateDelay()
                val cancelled = journey.legs?.get(0)?.cancelled

                logger.debug("departureDelay: $departureDelay")
                logger.debug("Journey: $journeys")
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
                                    text = "$departure",
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
                    DrawLegsLineWithIcons(legs, tripsState, tripsEvent, indexJourney)
                }
                Divider()
            }
        }
    }
}

@Composable
private fun DrawLegsLineWithIcons(
    legs: List<Leg>?,
    tripsState: TripsState,
    tripsEvent: suspend (TripsEvent) -> Unit,
    indexJourney: Int,
) {
    var expandedItemIndex by remember { mutableStateOf(-1) }
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expandedItemIndex = if (indexJourney == expandedItemIndex) -1 else indexJourney
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            legs?.forEach { leg ->
                val lineProductIcon = leg.line?.product?.getLineProductIcon() ?: 0
                val lineProductColor = leg.line?.product?.getLineProductColor() ?: Color.Transparent
                val lineNameIcon = leg.line?.name?.getLineNameIcon() ?: 0
                DrawLineProductImageJourneys(lineProductIcon, lineNameIcon)
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
        if (indexJourney == expandedItemIndex) {
            LegsColumn(
                legs = legs,
                tripsState,
                tripsEvent,
            )
        }
    }
}

@Composable
private fun DrawLineProductImageJourneys(lineProductIcon: Int, lineNameIcon: Int) {
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

@Composable
fun DisplayWarningRemark(warningRemark: Remark) {
    val summary = warningRemark.summary ?: ""
    val text = warningRemark.text ?: ""
    val validFrom = warningRemark.validFrom?.convertEpochDate() ?: ""
    val validUntil = warningRemark.validUntil?.convertEpochDate() ?: ""
    Box() {
        val scrollState = rememberScrollState()
        val textColor = if (isSystemInDarkTheme()) LightGray else DarkGray
        Column(
            modifier = Modifier.verticalScroll(scrollState),
        ) {
            Icon(
                painterResource(id = R.drawable.icon_warning_amber),
                contentDescription = null,
                modifier = Modifier.background(color = Color.Yellow)
                    .align(Alignment.CenterHorizontally).size(32.dp),
            )
            Text(
                text = summary,
                color = textColor,
                fontSize = 32.sp,
            )
            Text(
                text = text,
                color = textColor,
            )
            Text(
                text = "${stringResource(id = R.string.duration)}: $validFrom  $validUntil",
                color = textColor,
            )
        }
    }
}
//
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
//    JourneysColumn(
//        viewState = viewState,
//    ) {}
//    LegsColumn(
//        viewState = viewState,
//        onEvent = {},
//        legs = legs,
//    )
// }
