package com.example.berlingo.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.extensions.decodePolyline
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.columns.StopsQuerySection
import com.example.berlingo.map.columns.MapsJourneysColumn
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("MapsScreen()")

@Composable
fun MapsScreen(mapsState: State<MapsState>, journeysState: State<JourneysState>, onMapsEvent: suspend (MapsEvent) -> Unit, onJourneysEvent: suspend (JourneysEvent) -> Unit) {
    Surface(color = Color.Transparent, modifier = Modifier.fillMaxSize()) {
        val berlinLat = 52.5200
        val berlinLng = 13.4050
        val berlinCameraPosition = CameraPosition.fromLatLngZoom(LatLng(berlinLat, berlinLng), 10f)
        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            StopsQuerySection(
                journeysState,
                onJourneysEvent,
            )
            MapsJourneysColumn(
                journeysState,
                onMapsEvent,
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
            GoogleMap(
                modifier = Modifier.heightIn(800.dp),
                cameraPositionState = rememberCameraPositionState {
                    position = berlinCameraPosition
                },
            ) {
                //        val steps = mapsState.value.routes?.get(0)?.legs?.get(0)?.steps
                val latNE = mapsState.value.routes?.get(0)?.bounds?.northeast?.lat ?: 0.0
                val lngNE = mapsState.value.routes?.get(0)?.bounds?.northeast?.lng ?: 0.0
                val latSW = mapsState.value.routes?.get(0)?.bounds?.southwest?.lat ?: 0.0
                val lngSW = mapsState.value.routes?.get(0)?.bounds?.southwest?.lng ?: 0.0

                val latLingNE = LatLng(latNE, lngNE)
                val latLingSW = LatLng(latSW, lngSW)
                val latLingList = listOf(latLingNE, latLingSW)

                val points = mapsState.value.routes?.get(0)?.polyline?.points ?: ""
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
    }
}

@Preview
@Composable
fun MapsPreview() {
    val mockMapsState = remember { MapsState() }
    val mockJourneysState = remember { mutableStateOf(JourneysState()) }
    val mapsState = remember { mutableStateOf(mockMapsState) }
    val berlinLat = 52.5200
    val berlinLng = 13.4050
    val berlinCameraPosition = CameraPosition.fromLatLngZoom(LatLng(berlinLat, berlinLng), 10f)

    MapsScreen(mapsState = mapsState, journeysState = mockJourneysState, onMapsEvent = {}, onJourneysEvent = {})
}
