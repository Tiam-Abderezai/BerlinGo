import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("StopoversColumn()")

@Composable
fun TripsColumn(
    tripsState: TripsState,
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
    Box(modifier = Modifier.heightIn(max = 200.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = medium),
        ) {
            items(stopovers.toList()) { stopover ->
                val departureNotNull = !stopover?.departure.isNullOrEmpty()
                val stopNameNotNull = !stopover?.stop?.name.isNullOrEmpty()
                if (departureNotNull && stopNameNotNull) {
                    Text(
                        "${stopover?.departure?.convertEpochTime()} - ${stopover?.stop?.name}",
                        color = if (isSystemInDarkTheme()) LightGray else DarkGray,
                        modifier = Modifier
                            .padding(smallXX)
                            .clickable { logger.debug("trip.stopovers CLICKED}") },

                    )
                }
            }
        }
    }
}
