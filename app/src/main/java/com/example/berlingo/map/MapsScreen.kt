package com.example.berlingo.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.berlingo.common.extensions.decodePolyline
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapsScreen(mapsState: State<MapsState>, onEvent: suspend (MapsEvent) -> Unit) {
    Surface(color = Color.Transparent, modifier = Modifier.fillMaxSize()) {
        val berlinLat = 52.5200
        val berlinLng = 13.4050
        val berlinCameraPosition = CameraPosition.fromLatLngZoom(LatLng(berlinLat, berlinLng), 10f)
        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                onEvent.invoke(
                    MapsEvent.DirectionsGet(
                        origin = "Berlin",
                        destination = "Paris",
                        mode = "transit",
                        transitMode = "bus",
                        language = "en",
                    ),
                )
            }
        }
        Log.d("MapsScreen", "Direction: ${mapsState.value.routes}")

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState { position = berlinCameraPosition },
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
            Log.d("MapsScreen", "points: $points")

            Log.d("MapsScreen", "polyLine: $polyLine")

            Polyline(
                points = polyLine,
                color = Color.Blue,
                width = 5f,
            )
        }
    }
}

@Preview
@Composable
fun MapsPreview() {
    val mockMapsState = remember { MapsState() }
    val mapsState = remember { mutableStateOf(mockMapsState) }
    val berlinLat = 52.5200
    val berlinLng = 13.4050
    val berlinCameraPosition = CameraPosition.fromLatLngZoom(LatLng(berlinLat, berlinLng), 10f)

    MapsScreen(mapsState = mapsState) {}
}
