
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.extensions.getLineNameIcon
import com.example.berlingo.common.extensions.getLineProductColor
import com.example.berlingo.common.extensions.getLineProductIcon
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.utils.ErrorScreen
import com.example.berlingo.common.utils.LoadingScreen
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.trips.TripsEvent
import com.example.berlingo.trips.TripsState
import com.example.berlingo.trips.TripsState.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("LegsColumn()")

@Composable
fun LegsColumn(
    legs: List<Leg>?,
    tripsState: TripsState,
    tripsEvent: suspend (TripsEvent) -> Unit,
) {
    Box(modifier = Modifier.heightIn(max = 200.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
        ) {
            itemsIndexed(legs ?: emptyList()) { indexLeg, leg ->
                val line = legs?.get(indexLeg)?.line
                val lineProductIcon = line?.product?.getLineProductIcon() ?: 0
                val lineProductColor = line?.product?.getLineProductColor() ?: Color.Transparent
                val lineNameIcon = line?.name?.getLineNameIcon() ?: 0

                // 0 means ignore don't display Icon if no product is found
                logger.debug("line name: ${line?.name}")
                logger.debug("lineProductIcon: $lineProductIcon")
                logger.debug("lineNameIcon: $lineNameIcon")
                val tripId = leg.tripId ?: ""
                DrawLineProductImageLegs(leg, indexLeg, tripsState, tripsEvent)
            }
        }
    }
}

@Composable
private fun DrawLineProductImageLegs(
    leg: Leg,
    indexLeg: Int,
    tripsState: TripsState,
    tripsEvent: suspend (TripsEvent) -> Unit,
) {
    var expandedItemIndex by remember { mutableStateOf(-1) }
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                expandedItemIndex = if (indexLeg == expandedItemIndex) -1 else indexLeg
                CoroutineScope(Dispatchers.IO).launch {
                    tripsEvent.invoke(
                        TripsEvent.TripQueryEvent(
                            leg.tripId ?: "",
                        ),
                    )
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val lineProductIcon = leg.line?.product?.getLineProductIcon() ?: 0
        val lineProductColor = leg.line?.product?.getLineProductColor() ?: Color.Transparent
        val lineNameIcon = leg.line?.name?.getLineNameIcon() ?: 0
        DrawLineProductIcons(lineProductIcon, lineNameIcon)
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
    if (indexLeg == expandedItemIndex) {
        TripsColumn(
            tripsState = tripsState,
            tripsEvent = tripsEvent,
        )
    }
}

@Composable
fun DrawLineProductIcons(lineProductIcon: Int, lineNameIcon: Int) {
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
// fun LegsColumnPreview() {
//    val viewState = remember { mutableStateOf(JourneysState()) }
//
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
//    LegsColumn(
//        viewState = viewState,
//        onEvent = {},
//        legs = legs,
//    )
// }
