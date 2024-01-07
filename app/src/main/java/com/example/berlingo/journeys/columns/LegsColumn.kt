import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.berlingo.data.network.responses.Destination
import com.example.berlingo.data.network.responses.Journey
import com.example.berlingo.data.network.responses.Leg
import com.example.berlingo.data.network.responses.Origin
import com.example.berlingo.data.network.responses.Trip
import com.example.berlingo.journeys.JourneysViewEvent
import com.example.berlingo.journeys.JourneysViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LegsColumn(
    viewState: State<JourneysViewState>,
    onEvent: suspend (JourneysViewEvent) -> Unit,
) {
    var expandedItemIndex by remember { mutableStateOf(-1) }
    val journeys = viewState.value.journeys
    val legs = viewState.value.legs
//    var searchedTrips1 = searchedTrips
    Box(modifier = Modifier.heightIn(max = 200.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            itemsIndexed(legs?.entries?.toList() ?: emptyList()) { indexLeg, leg ->

//                Log.d("dev-log", "show leg ${leg.key.?.name}")

                Text(
                    "${leg.key.origin?.name}",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            expandedItemIndex =
                                if (indexLeg == expandedItemIndex) -1 else indexLeg
                            CoroutineScope(Dispatchers.IO).launch {
//                                searchedTrips1 = viewState.searchedTrips.value
                            }
                        },
                )
                if (indexLeg == expandedItemIndex) {
                    StopoversColumn(
                        viewState = viewState,
                    onEvent = onEvent,
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LegsColumnPreview() {
    val viewState = remember { mutableStateOf(JourneysViewState()) }

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

    LegsColumn(
        viewState = viewState,
        onEvent = {},
    )
}