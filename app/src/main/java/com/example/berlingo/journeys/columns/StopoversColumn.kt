import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("StopoversColumn()")

@Composable
fun StopoversColumn(
    viewState: State<JourneysState>,
    onEvent: suspend (JourneysEvent) -> Unit,
) {
    val trip = viewState.value.trip
    logger.debug("show trip ${trip?.stopovers}")

    Box(modifier = Modifier.heightIn(max = 200.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            items(trip?.stopovers?.toList() ?: emptyList()) { stopovers ->
                logger.debug("trip.stopovers === $stopovers}")
                Text(
                    "Stopovers: ${stopovers.stop?.name}",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            logger.debug("trip.stopovers CLICKED}")

//                                                        expandedLegItemIndex = if (index == expandedLegItemIndex) -1 else index
                        },
                )
            }
        }
    }
}