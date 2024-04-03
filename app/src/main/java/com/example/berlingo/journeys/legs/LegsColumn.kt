import android.content.res.Configuration
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions.medium
import com.example.berlingo.common.Dimensions.small
import com.example.berlingo.common.extensions.getLineNameIcon
import com.example.berlingo.common.extensions.getLineProductColor
import com.example.berlingo.common.extensions.getLineProductIcon
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.legs.stops.network.responses.Location
import com.example.berlingo.journeys.network.responses.Destination
import com.example.berlingo.journeys.network.responses.Leg
import com.example.berlingo.journeys.network.responses.Line
import com.example.berlingo.journeys.network.responses.Origin
import com.example.berlingo.trips.TripsEvent
import com.example.berlingo.trips.TripsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("LegsColumn()")
private const val testTag = "LegsColumn()"

@Composable
fun LegsColumn(
    legs: List<Leg>? = emptyList(),
    tripsState: TripsState = TripsState.Initial,
    tripsEvent: suspend (TripsEvent) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .testTag("$testTag: Box()")
            .heightIn(max = 200.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag("$testTag: Box(): LazyColumn()")
                .fillMaxWidth()
                .padding(start = medium),
        ) {
            itemsIndexed(legs ?: emptyList()) { indexLeg, leg ->
                val line = legs?.get(indexLeg)?.line
                logger.debug("line name: ${line?.name}")
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
    val line = leg.line
    val product = line?.product
    val walking = leg.walking
    val lineProductIcon =
        if (walking == true) R.drawable.icon_change_station else product?.getLineProductIcon() ?: 0
    val lineProductColor =
        if (walking == true) Color.Black else product?.getLineProductColor() ?: Color.Transparent
    val lineNameIcon =
        if (walking == true) R.drawable.icon_walking else line?.name?.getLineNameIcon() ?: 0
    Row(
        modifier = Modifier
            .testTag("$testTag: DrawLineProductImageLegs(): Row()")
            .fillMaxWidth()
            // Don't show trips if walking is true, because there is nothing to show
            .clickable(walking == false) {
                expandedItemIndex = if (indexLeg == expandedItemIndex) -1 else indexLeg
                CoroutineScope(Dispatchers.IO).launch {
                    tripsEvent.invoke(
                        TripsEvent.TripQueryEvent(
                            tripId = leg.tripId ?: "",
                            leg = leg,
                        ),
                    )
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DrawLineProductIcons(lineProductIcon, lineNameIcon)
        Canvas(
            modifier = Modifier
                .testTag("$testTag: DrawLineProductImageLegs(): Row() - Canvas()")
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
    // Don't show trips if walking is true, because there is nothing to show
    if (indexLeg == expandedItemIndex && walking == false) {
        TripsColumn(
            tripsState = tripsState,
        )
    }
}

@Composable
fun DrawLineProductIcons(lineProductIcon: Int, lineNameIcon: Int) {
    // 0 means ignore don't display Icon if no product is found
    if (lineProductIcon != 0 && lineNameIcon != 0) {
        Image(
            modifier = Modifier
                .testTag("$testTag: DrawLineProductIcons(): Image() - lineProductIcon")
                .size(medium),
            painter = painterResource(id = lineProductIcon),
            contentDescription = null,
        )
        Image(
            modifier = Modifier
                .testTag("$testTag: DrawLineProductIcons(): Image() - lineNameIcon")
                .size(medium),
            painter = painterResource(id = lineNameIcon),
            contentDescription = null,
        )
    }
}

 @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
 @Composable
 fun LegsColumnPreview() {
     val leg1 = Leg(origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
         destination = Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)", location = Location()),
         plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11", line = Line(name = "M85", product = "bus")
     )
     val leg2 = Leg(origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
         destination = Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)", location = Location()),
         plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11", line = Line(name = "M48", product = "bus")
     )
     val leg3 = Leg(origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
         destination = Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)", location = Location()),
         plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11", line = Line(name = "RE8", product = "regional")
     )
     val leg4 = Leg(origin = Origin("S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
         destination = Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)", location = Location()),
         plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11", line = Line(name = "S8", product = "suburban")
     )
     val legs = listOf(leg1, leg2, leg3, leg4)

    LegsColumn(legs = legs)
 }
