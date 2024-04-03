import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions.medium
import com.example.berlingo.common.Dimensions.small
import com.example.berlingo.common.Dimensions.smallX
import com.example.berlingo.common.components.Divider
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.extensions.convertEpochDate
import com.example.berlingo.common.extensions.convertEpochTime
import com.example.berlingo.common.extensions.getLineNameIcon
import com.example.berlingo.common.extensions.getLineProductColor
import com.example.berlingo.common.extensions.getLineProductIcon
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.legs.stops.network.responses.Location
import com.example.berlingo.journeys.network.responses.Destination
import com.example.berlingo.journeys.network.responses.Journey
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.journeys.network.responses.Line
import com.example.berlingo.journeys.network.responses.Origin
import com.example.berlingo.journeys.network.responses.Remark
import com.example.berlingo.trips.TripsEvent
import com.example.berlingo.trips.TripsState
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import com.example.berlingo.ui.theme.isDarkMode

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("JourneysColumn()")
private const val testTag = "JourneysColumn()"

@Composable
fun JourneysColumn(
    journeysState: JourneysState = JourneysState.Initial,
    journeyEvent: suspend (JourneysEvent) -> Unit = {},
    tripsState: TripsState = TripsState.Initial,
    tripsEvent: suspend (TripsEvent) -> Unit = {},
) {
    when (journeysState) {
        is JourneysState.Initial -> {}
        is JourneysState.Loading -> LoadingScreen()
        is JourneysState.Error -> ErrorScreen(message = journeysState.message)
        is JourneysState.Success -> {
            Box(modifier = Modifier.testTag("$testTag: Box()")) {
                Column(modifier = Modifier.testTag("$testTag: Box(): Column()")) {
                    if (journeysState.warningRemark?.type == "warning") {
                        DisplayWarningRemark(journeysState.warningRemark)
                    }
                    DisplayJourneys(journeysState.journeys, journeyEvent, tripsState, tripsEvent)
                }
            }
            Spacer(modifier = Modifier.height(smallX))
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
    Box(modifier = Modifier.testTag("$testTag: DisplayJourneys(): Box()")) {
        val textColor = if (isDarkMode()) LightGray else DarkGray
        LazyColumn(
            modifier = Modifier
                .testTag("$testTag: DisplayJourneys(): Box(): LazyColumn()")
                .fillMaxSize(),
        ) {
            itemsIndexed(journeys.keys.toList()) { indexJourney, journey ->
                val legs = journey.legs
                val plannedDeparture =
                    journey.legs?.get(0)?.plannedDeparture?.convertEpochTime().toString()
                val departure = journey.legs?.get(0)?.departure?.convertEpochTime().toString()
                val departureDelay = journey.legs?.get(0)?.departureDelay
                val cancelled = journey.legs?.get(0)?.cancelled
                logger.debug("departureDelay: $departureDelay")
                logger.debug("Journey: $journeys")
                logger.debug("Legs: ${journey.legs}")
                logger.debug("TripIds: ${journey.legs?.get(0)?.tripId}")
                Row(modifier = Modifier.testTag("$testTag: DisplayJourneys(): Box(): LazyColumn(): Row()")) {
                    if (cancelled == false) {
                        Column(modifier = Modifier.testTag("$testTag: DisplayJourneys(): Box(): LazyColumn(): Row(): LazyColumn()")) {
                            Text(
                                modifier = Modifier.testTag("$testTag: DisplayJourneys(): Box(): LazyColumn(): Row(): LazyColumn(): Text() - plannedDeparture"),
                                text = plannedDeparture,
                                color = textColor,
                            )
                            if (departureDelay != 0 && departureDelay != null) {
                                Text(
                                    modifier = Modifier.testTag("$testTag: DisplayJourneys(): Box(): LazyColumn(): Row(): LazyColumn(): Text() - departure"),
                                    text = departure,
                                    color = Color.Red,
                                )
                                Text(
                                    modifier = Modifier.testTag("$testTag: DisplayJourneys(): Box(): LazyColumn(): Row(): LazyColumn(): Text() - departureDelay"),
                                    text = "($departureDelay)",
                                    color = Color.Red,
                                )
                            }
                        }
                    } else {
                        Text(
                            modifier = Modifier.testTag("$testTag: DisplayJourneys(): Box(): LazyColumn(): Row(): Text() - plannedDeparture"),
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
    Column(modifier = Modifier.testTag("$testTag: DrawLegsLineWithIcons(): Column()")) {
        Row(
            modifier = Modifier
                .testTag("$testTag: DrawLegsLineWithIcons(): Column(): Row()")
                .fillMaxWidth()
                .clickable {
                    expandedItemIndex = if (indexJourney == expandedItemIndex) -1 else indexJourney
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            legs?.forEach { leg ->
                val line = leg.line
                val product = line?.product
                val walking = leg.walking
                val lineProductIcon =
                    if (walking == true) R.drawable.icon_change_station else product?.getLineProductIcon()
                        ?: 0
                val lineProductColor =
                    if (walking == true) Color.Black else product?.getLineProductColor()
                        ?: Color.Transparent
                val lineNameIcon =
                    if (walking == true) R.drawable.icon_walking else line?.name?.getLineNameIcon()
                        ?: 0
                DrawLineProductImageJourneys(lineProductIcon, lineNameIcon)
                Canvas(
                    modifier = Modifier
                        .testTag("$testTag: DrawLegsLineWithIcons(): Column(): Row(): Canvas()")
                        .weight(1f)
                        .height(small),
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
            modifier = Modifier
                .testTag("$testTag: DrawLineProductImageJourneys(): Image() - lineProduction")
                .size(medium),
            painter = painterResource(id = lineProductIcon),
            contentDescription = null,
        )
        Image(
            modifier = Modifier
                .testTag("$testTag: DrawLineProductImageJourneys(): Image() - lineNameIcon")
                .size(medium),
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
    Box(modifier = Modifier.testTag("$testTag: DisplayWarningRemark(): Box()")) {
        val scrollState = rememberScrollState()
        val textColor = if (isDarkMode()) LightGray else DarkGray
        Column(
            modifier = Modifier
                .testTag("$testTag: DisplayWarningRemark(): Box(): Column()")
                .verticalScroll(scrollState),
        ) {
            Icon(
                modifier = Modifier
                    .testTag("$testTag: DisplayWarningRemark(): Box(): Column(): Icon()")
                    .background(color = Color.Yellow)
                    .align(Alignment.CenterHorizontally)
                    .size(medium),
                painter = painterResource(id = R.drawable.icon_warning_amber),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.testTag("$testTag: DisplayWarningRemark(): Box(): Column(): Text() - summary"),
                text = summary,
                color = textColor,
                fontSize = 32.sp,
            )
            Text(
                modifier = Modifier.testTag("$testTag: DisplayWarningRemark(): Box(): Column(): Text() - text"),
                text = text,
                color = textColor,
            )
            Text(
                modifier = Modifier.testTag("$testTag: DisplayWarningRemark(): Box(): Column(): Text() - Duration"),
                text = "${stringResource(id = R.string.duration)}: $validFrom  $validUntil",
                color = textColor,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun JourneysColumnPreview() {
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
    JourneysColumn(journeysState = JourneysState.Success(journeys))
}
