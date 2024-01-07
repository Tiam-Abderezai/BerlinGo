import android.util.Log
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
import com.example.berlingo.data.network.responses.Trip
import com.example.berlingo.journeys.JourneysViewEvent
import com.example.berlingo.journeys.JourneysViewState

@Composable
fun StopoversColumn(
    viewState: State<JourneysViewState>,
    onEvent: suspend (JourneysViewEvent) -> Unit,
) {
    val trip = viewState.value.trip
    Log.d("dev-log", "show trip ${trip?.stopovers}")
    Box(modifier = Modifier.heightIn(max = 200.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            items(trip?.stopovers?.toList() ?: emptyList()) { stopovers ->
                Log.d("dev-log", "trip.stopovers === $stopovers}")
                Text(
                    "Stopovers: ${stopovers.stop?.name}",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
//                                                        expandedLegItemIndex = if (index == expandedLegItemIndex) -1 else index
                        },
                )
            }
        }
    }
}