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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.extensions.getDepartureTime
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.utils.ErrorScreen
import com.example.berlingo.common.utils.LoadingScreen
import com.example.berlingo.trips.TripsEvent
import com.example.berlingo.trips.TripsState
import com.example.berlingo.trips.network.responses.Trip

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("StopoversColumn()")

@Composable
fun TripsColumn(
    tripsState: TripsState,
    tripsEvent: suspend (TripsEvent) -> Unit,
) {
    when (tripsState) {
        is TripsState.Initial -> {}
        is TripsState.Loading -> LoadingScreen()
        is TripsState.Error -> ErrorScreen(message = tripsState.message ?: "Unknown Error")
        is TripsState.Success -> DisplayTrips(tripsState.tripData, tripsEvent)
    }
}

@Composable
private fun DisplayTrips(trip: Trip?, tripsEvent: suspend (TripsEvent) -> Unit) {
    Box(modifier = Modifier.heightIn(max = 200.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
        ) {
            items(trip?.stopovers?.toList() ?: emptyList()) { stopovers ->
                logger.debug("trip.stopovers === $stopovers}")
                Text(
                    "${stopovers.departure?.getDepartureTime()} - ${stopovers.stop?.name}",
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { logger.debug("trip.stopovers CLICKED}") },
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }
}
