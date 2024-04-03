import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.Dimensions.medium
import com.example.berlingo.common.Dimensions.smallXX
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.extensions.convertEpochTime
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.trips.TripsState
import com.example.berlingo.trips.network.responses.Trip
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import com.example.berlingo.ui.theme.isDarkMode

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("StopoversColumn()")
private const val testTag = "TripsColumn()"

@Composable
fun TripsColumn(
    tripsState: TripsState = TripsState.Initial,
) {
    when (tripsState) {
        is TripsState.Initial -> {}
        is TripsState.Loading -> LoadingScreen()
        is TripsState.Error -> ErrorScreen(message = tripsState.message)
        is TripsState.Success -> DisplayTrips(
            tripsState.stopoversData ?: emptyList(),
        )
    }
}

@Composable
private fun DisplayTrips(
    stopovers: List<Trip.Stopover?>,
) {
    Box(
        modifier = Modifier
            .testTag("$testTag: DisplayTrips(): Box()")
            .heightIn(max = 200.dp)) {
        LazyColumn(
            modifier = Modifier
                .testTag("$testTag: DisplayTrips(): Box(): LazyColumn()")
                .fillMaxWidth()
                .padding(start = medium),
        ) {
            items(stopovers.toList()) { stopover ->
                val departureNotNull = !stopover?.departure.isNullOrEmpty()
                val stopNameNotNull = !stopover?.stop?.name.isNullOrEmpty()
                if (departureNotNull && stopNameNotNull) {
                    Text(
                        modifier = Modifier
                            .testTag("$testTag: DisplayTrips(): Box(): LazyColumn(): Text()")
                            .padding(smallXX)
                            .clickable { logger.debug("trip.stopovers CLICKED}") },
                        text = "${stopover?.departure?.convertEpochTime()} - ${stopover?.stop?.name}",
                        color = if (isDarkMode()) LightGray else DarkGray,
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun TripsColumnPreview() {
    val trip1 = Trip(origin = Trip.Origin(id = "trip", name = "S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Trip.Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)"),
        plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11")
    val trip2 = Trip(origin = Trip.Origin(id = "trip", name = "S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Trip.Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)"),
        plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11")
    val trip3 = Trip(origin = Trip.Origin(id = "trip", name = "S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Trip.Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)"),
        plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11")
    val trip4 = Trip(origin = Trip.Origin(id = "trip", name = "S+U Rathaus Steglitz (Berlin) [Schloßstr.]"),
        destination = Trip.Destination(name = "S Potsdamer Platz Bhf/Voßstr. (Berlin)"),
        plannedDeparture = "2024-04-03T01:26:00+02:00", departure = "11:11")
    val trips = listOf(trip1, trip2, trip3, trip4)
    TripsColumn(tripsState = TripsState.Success(trips))
}