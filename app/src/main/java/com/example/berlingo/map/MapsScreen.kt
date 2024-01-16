package com.example.berlingo.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.extensions.decodePolyline
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.common.utils.ErrorScreen
import com.example.berlingo.common.utils.LoadingScreen
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.legs.stops.StopsColumn
import com.example.berlingo.journeys.legs.stops.StopsEvent
import com.example.berlingo.journeys.legs.stops.StopsState
import com.example.berlingo.map.MapsState.*
import com.example.berlingo.map.columns.MapsJourneysColumn
import com.example.berlingo.map.network.responses.Route

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("MapsScreen()")

// @Composable
// fun MapsScreenMain(
//    mapsState: MapsState<List<Route>>,
//    mapsEvent: suspend (MapsEvent) -> Unit,
//    journeysState: JourneysState<Map<Journey, List<Leg>>>,
//    journeysEvent: suspend (JourneysEvent) -> Unit,
// ) {
//    when (mapsState) {
//        is MapsState.Loading -> LoadingScreen()
//        is MapsState.Success -> MapsScreen(mapsState.data, mapsEvent, journeysState, journeysEvent)
//        is MapsState.Error -> ErrorScreen(mapsState.exception)
//    }
// }

@Composable
fun MapsScreen(
    mapsState: MapsState,
    mapsEvent: suspend (MapsEvent) -> Unit,
    journeysState: JourneysState,
    journeysEvent: suspend (JourneysEvent) -> Unit,
    stopsState: StopsState,
    stopsEvent: suspend (StopsEvent) -> Unit,
) {
//    val stopsViewModel = hiltViewModel<StopsViewModel>()
//    val stopsState = stopsViewModel.state.collectAsState().value
//    val stopsEvent = stopsViewModel::handleEvent
    Surface(color = Color.Transparent, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            StopsColumn(
                journeysState,
                journeysEvent,
                stopsState,
                stopsEvent,
            )
            MapsJourneysColumn(
                journeysState,
                mapsEvent,
            )

            // ------------------------------ //
            // Hardcoded Values for Debugging //

//            LaunchedEffect(Unit) {
//                CoroutineScope(Dispatchers.IO).launch {
//                    onMapsEvent.invoke(
//                        MapsEvent.DirectionsGet(
//                            origin = "Berlin",
//                            destination = "Paris",
//                            mode = "transit",
//                            transitMode = "bus",
//                            language = "en",
//                        ),
//                    )
//                }
//            }
//            logger.debug("Directions: ${mapsState.value.routes}")
            when (mapsState) {
                is Initial -> {}
                is Loading -> LoadingScreen()
                is Error -> ErrorScreen(message = mapsState.message)
                is Success -> DisplayGoogleMaps(routes = mapsState.data)
            }
        }
    }
}

@Composable
private fun DisplayGoogleMaps(routes: List<Route>) {
    val berlinLat = 52.5200
    val berlinLng = 13.4050
    val berlinCameraPosition = CameraPosition.fromLatLngZoom(LatLng(berlinLat, berlinLng), 10f)
    GoogleMap(
        modifier = Modifier.heightIn(800.dp),
        cameraPositionState = rememberCameraPositionState {
            position = berlinCameraPosition
        },
    ) {
        //        val steps = mapsState.value.routes?.get(0)?.legs?.get(0)?.steps
        val latNE = routes[0].bounds?.northeast?.lat ?: 0.0
        val lngNE = routes[0].bounds?.northeast?.lng ?: 0.0
        val latSW = routes[0].bounds?.southwest?.lat ?: 0.0
        val lngSW = routes[0].bounds?.southwest?.lng ?: 0.0

        val latLingNE = LatLng(latNE, lngNE)
        val latLingSW = LatLng(latSW, lngSW)
        val latLingList = listOf(latLingNE, latLingSW)

        val points = routes[0].polyline?.points ?: ""
        val polyLine = points.decodePolyline()

        //                logger.debug("points: $points")
        //                logger.debug("polyLine: $polyLine")

        Polyline(
            points = polyLine,
            color = Color.Blue,
            width = 5f,
        )
    }
}
// @Preview
// @Composable
// fun MapsPreview() {
//    val mockMapsState = remember { MapsState() }
//    val mockJourneysState = remember { mutableStateOf(JourneysState()) }
//    val mapsState = remember { mutableStateOf(mockMapsState) }
//    val berlinLat = 52.5200
//    val berlinLng = 13.4050
//    val berlinCameraPosition = CameraPosition.fromLatLngZoom(LatLng(berlinLat, berlinLng), 10f)
//
//    MapsScreen(mapsState = mapsState, journeysState = mockJourneysState, onMapsEvent = {}, onJourneysEvent = {})
// }
